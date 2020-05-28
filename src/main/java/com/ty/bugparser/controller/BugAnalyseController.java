package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.pojo.Testcase;
import com.ty.bugparser.utils.Executor;
import com.ty.bugparser.service.SuspiciousResultsService;
import com.ty.bugparser.service.TestcaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/BugAnalyse")
public class BugAnalyseController {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

    @Autowired
    private TestcaseService testcaseService;

    @Autowired
    private Executor executor;

    /**
     * 统计所有可疑用例数目，以及其中已分析和未分析的数目
     * @param model 数据模型
     * @return 跳转到用例分析的页面
     */
    @RequestMapping("/BugAnalyse")
    public String countNumber(Model model) {
        // 获取全部结果
        List<SuspiciousResults> allResults = suspiciousResultsService.queryAllSuspiciousResults();
        int allNumber = allResults.size();
        int analysedNumber = 0;
        int noAnalysedNumber = 0;

        // 遍历一遍结果，获取到统计数据
        for (SuspiciousResults record : allResults) {
            if (record.getAssignee() != null) {
                analysedNumber += 1;
            } else {
                noAnalysedNumber += 1;
            }
        }

        // 将结果写入model
        model.addAttribute("allNumber", allNumber);
        model.addAttribute("analysedNumber", analysedNumber);
        model.addAttribute("noAnalysedNumber", noAnalysedNumber);

        return "/BugAnalyse/BugAnalyse.html";
    }

    /**
     * 从未分析的可疑用例中随机挑一个，并返回其相关的三个ID值，组装成Map返回
     * @return 将需要的四部分信息组装成Map返回
     */
    @RequestMapping("/getATestcase")
    @ResponseBody
    public String getATestcase() {
        int suspiciousId = suspiciousResultsService.queryRandomNoAnalysedSuspiciousId();
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
     * 提交用例，主要是给Sus表中填充字段Assignee，以及Cases表中填充精简后的字符串
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
            updateAssigneeResult = suspiciousResultsService.updateSuspiciousResults(suspiciousResult);
        }

        // 向Testcases的对应记录中填充simplifiedCase信息
        Testcase testcase = testcaseService.queryTestcaseById(Integer.parseInt(testcaseId));
        int updateTestcaseResult = 0;
        if (testcase != null) {
            testcase.setSimplified_case(simplifiedCase);
            updateTestcaseResult = testcaseService.updateTestcase(testcase);
        }

        // 假如两个操作都成功了，才返回 "1"，表示提交成功
        String flag = "0";
        if (updateAssigneeResult == 1 && updateTestcaseResult == 1) {
            flag = "1";
        }

        return flag;
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
}
