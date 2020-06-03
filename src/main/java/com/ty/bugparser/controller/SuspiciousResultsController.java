package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.OriginalTestResult;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.pojo.Testcase;
import com.ty.bugparser.service.OriginalTestResultService;
import com.ty.bugparser.service.SuspiciousResultsService;
import com.ty.bugparser.service.TestcaseService;
import com.ty.bugparser.utils.Executor;
import com.ty.bugparser.utils.Timer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/SuspiciousResults")
@ConfigurationProperties("user.gettestcase")
@Data
public class SuspiciousResultsController {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

    @Autowired
    private Executor executor;

    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private OriginalTestResultService originalTestResultService;

    @Autowired
    private Timer timer;

    /**
     * lockedSuspiciousIds:临时缓存，用来防止多用户重复读同一条数据
     * key：SuspiciousId
     * value：该Id被读取时的时间（毫秒数）
     */
    private static Map<Integer, Long> lockedSuspiciousIds = new HashMap<>();

    // 这两个值将从配置文件注入
    private int timeLimitHours;
    private int maxUsersCount;

    /**
     * 统计所有可疑用例数目，以及其中已分析和未分析的数目
     * @return 跳转到用例分析的页面
     */
    @RequestMapping("/Analyse")
    public String countNumber(HttpSession session, Model model) {
        // 获取全部结果
        List<SuspiciousResults> allResults = suspiciousResultsService.queryAllSuspiciousResults();
        int allNumber = allResults.size();
        int analysedNumber = 0;
        int noAnalysedNumber = 0;
        int todayAnalysedNumber = 0;
        String loginUser = (String)session.getAttribute("loginUser");

        // 遍历一遍结果，获取到统计数据
        for (SuspiciousResults record : allResults) {
            if (record.getAssignee() != null) {
                analysedNumber += 1;
                if (record.getSubmit_date() != 0L
                        && timer.isSameDayOfMillis(record.getSubmit_date(), System.currentTimeMillis())
                        && loginUser.equals(record.getAssignee())) {
                    todayAnalysedNumber += 1;
                }
            } else {
                noAnalysedNumber += 1;
            }
        }

        // 将结果写入model
        model.addAttribute("allNumber", allNumber);
        model.addAttribute("analysedNumber", analysedNumber);
        model.addAttribute("noAnalysedNumber", noAnalysedNumber);
        model.addAttribute("todayAnalysedNumber", todayAnalysedNumber);
        model.addAttribute("lockedTestcasesNumber", lockedSuspiciousIds.size());

        return "/BugAnalyse/BugAnalyse";
    }

    @RequestMapping("/queryAll")
    @ResponseBody
    public String queryAll() {
        List<SuspiciousResults> suspiciousResults = suspiciousResultsService.queryAllSuspiciousResults();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", suspiciousResults.size());
        map.put("data", suspiciousResults);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/queryAllAnalysed")
    @ResponseBody
    public String queryAllAnalysed() {
        List<SuspiciousResults> suspiciousResults = suspiciousResultsService.queryAllAnalysed();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", suspiciousResults.size());
        map.put("data", suspiciousResults);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/queryAllNoAnalysed")
    @ResponseBody
    public String queryAllNoAnalysed() {
        List<SuspiciousResults> suspiciousResults = suspiciousResultsService.queryAllNoAnalysed();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", suspiciousResults.size());
        map.put("data", suspiciousResults);

        return JSON.toJSONString(map);
    }

    /**
     * 从未分析的可疑用例中随机挑一个，并返回其相关的三个ID值，组装成Map返回
     * @return 将需要的四部分信息组装成Map返回
     */
    @RequestMapping("/getATestcase")
    @ResponseBody
    public String getATestcase() {
        // lazy-check策略：只在每一次读用例之前，检查一下是否有锁定超时的情况：有的话解锁；没有的话再正常读；
        List<Integer> overtimeSuspiciousIds = new ArrayList<>();
        // timeLimitHours：超时时间（单位：小时）
        for (Map.Entry<Integer, Long> entry : lockedSuspiciousIds.entrySet()) {
            if (Math.abs(entry.getValue() - System.currentTimeMillis()) > timeLimitHours * 60 * 60 * 1000) {
                overtimeSuspiciousIds.add(entry.getKey());
            }
        }
        for (Integer sId : overtimeSuspiciousIds) {
            lockedSuspiciousIds.remove(sId);
        }

        // 要保证当前读的suspiciousId是未被锁定的，并在读出后将其锁定
        int suspiciousId;
        int count = 0;
        do {
            suspiciousId = suspiciousResultsService.queryRandomNoAnalysedSuspiciousId();
            if (count++ > maxUsersCount) {
                suspiciousId = -1;
                break;
            }
        } while(lockedSuspiciousIds.containsKey(suspiciousId));

        if (suspiciousId != -1) {
            lockedSuspiciousIds.put(suspiciousId, System.currentTimeMillis());
        }

        int harnessId = suspiciousResultsService.queryHarnessIdBySuspiciousId(suspiciousId);
        int testcaseId = suspiciousResultsService.queryTestcaseIdByHarnessId(harnessId);
        String testcase = suspiciousResultsService.queryTestcaseCodeByTestcaseId(testcaseId);

        Map<String, String> map = new HashMap<>(4);
        map.put("suspiciousId", String.valueOf(suspiciousId));
        map.put("harnessId", String.valueOf(harnessId));
        map.put("testcaseId", String.valueOf(testcaseId));
        map.put("testcase", testcase);
        return JSON.toJSONString(map);
    }

    /**
     * 执行脚本，对传入测试用例进行测试，并返回脚本执行的结果
     * @param code 测试用例的代码
     * @return 脚本执行该测试用例的结果字符串
     */
    @RequestMapping("/run")
    @ResponseBody
    public String run(String code) {
        return executor.executeScript(code);
    }

    /**
     * 将用例复原
     * @param testcaseId 要复原的用例ID
     * @return 复原后的测试用例代码
     */
    @RequestMapping("/recoverTestcase")
    @ResponseBody
    public String recoverTestcase(String testcaseId) {
        return suspiciousResultsService.queryTestcaseCodeByTestcaseId(Integer.parseInt(testcaseId));
    }

    /**
     * 提交用例，主要是给Sus表中填充字段assignee和submit_date，以及Cases表中填充精简后的字符串
     * @param suspiciousId 可疑用例的ID
     * @param testcaseId 可疑用例对应的Testcases表中的ID
     * @param simplifiedCase 精简后的测试用例代码
     * @param assignee 被委托人，即对该用例进行分析的人
     * @return flag 字符串，"0"表示提交失败，"1"表示提交成功
     */
    @RequestMapping("/submitTestcase")
    @ResponseBody
    public String submitTestcase(String suspiciousId,
                                 String testcaseId,
                                 String simplifiedCase,
                                 String assignee) {

        // 向SuspiciousResults的对应记录中填充assignee信息，将其视为分析过的
        SuspiciousResults suspiciousResult = suspiciousResultsService.querySuspiciousResultsById(Integer.parseInt(suspiciousId));
        int updateAssigneeResult = 0;
        if (suspiciousResult != null) {
            suspiciousResult.setAssignee(assignee);
            suspiciousResult.setSubmit_date(System.currentTimeMillis());
            updateAssigneeResult = suspiciousResultsService.updateSuspiciousResults(suspiciousResult);
        }

        // 向Testcases的对应记录中填充simplifiedCase信息
        Testcase testcase = testcaseService.queryTestcaseById(Integer.parseInt(testcaseId));
        int updateTestcaseResult = 0;
        if (testcase != null) {
            testcase.setSimplified_case(simplifiedCase);
            updateTestcaseResult = testcaseService.updateTestcase(testcase);
        }

        // 假如两个操作都成功了，先解锁，再返回 "1"，表示提交成功
        String flag = "0";
        if (updateAssigneeResult == 1 && updateTestcaseResult == 1) {
            lockedSuspiciousIds.remove(Integer.valueOf(suspiciousId));
            flag = "1";
        }

        return flag;
    }

    @RequestMapping("/getOriginalResult/{suspiciousId}")
    @ResponseBody
    public String getOriginalTestResult(@PathVariable String suspiciousId) {
        // 获取结果列表
        int harnessId = suspiciousResultsService.queryHarnessIdBySuspiciousId(Integer.parseInt(suspiciousId));
        List<OriginalTestResult> originalTestResult = originalTestResultService.queryOriginalTestResultByHarnessId(harnessId);

        // 将出问题的那个结果的对象的isBug字段赋值为1
        for (OriginalTestResult testResult : originalTestResult) {
            if (testResult.getHarnessId() == harnessId) {
                testResult.setIsBug(1);
            }
        }

        Map<String, Object> map = new HashMap<>(4);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", originalTestResult.size());
        map.put("data", originalTestResult);

        return JSON.toJSONString(map);
    }

    @RequestMapping("/queryTestcase/{keyword}")
    @ResponseBody
    public String querySuspiciousTestcaseByKeyword(@PathVariable String keyword) {
        List<Testcase> testcaseResults = suspiciousResultsService.querySuspiciousTestcaseByKeyword(keyword);

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", testcaseResults.size());
        map.put("data", testcaseResults);

        return JSON.toJSONString(map);

    }
}
