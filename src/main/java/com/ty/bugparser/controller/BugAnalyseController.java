package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.pojo.Testcase;
import com.ty.bugparser.service.ExecutorImpl;
import com.ty.bugparser.service.SuspiciousResultsService;
import com.ty.bugparser.service.TestcaseService;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping("/run")
    @ResponseBody
    public String run(String code) {
        // 以下为生产环境
        log.warn("传过来的code为:" + code);
        ExecutorImpl executor = new ExecutorImpl();
        String testcaseFileName = executor.writeInFile(code);
        String prefix = "bash";
        String bashFileName = "/export/nisl/yhy/javascriptFuzzingOther/test/ty.sh";
        log.warn("新建的测试用例名称为:" + testcaseFileName);

        String result = "";
        try {
            result = executor.executeScript(prefix, bashFileName, testcaseFileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 删除该测试用例临时文件
            File tempFile = new File(testcaseFileName);
            if (tempFile.exists() && tempFile.isFile()) {
                tempFile.delete();
            }
        }
        log.warn("执行完指令的result为:" + result);
        return result;
    }

    @RequestMapping("/submitTestcase")
    @ResponseBody
    public String submitTestcase(String suspiciousId,
                                 String testcaseId,
                                 String simplifiedCase,
                                 String assignee) {

        System.out.println("suspiciousId:" + Integer.parseInt(suspiciousId));
        System.out.println("testcaseId:" + Integer.parseInt(testcaseId));
        System.out.println("assignee:" + assignee);
        System.out.println(simplifiedCase);

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

        System.out.println("返回值为:" + flag);
        return flag;
    }

    @RequestMapping("recoverTestcase")
    @ResponseBody
    public String recoverTestcase(String testcaseId) {
        return suspiciousResultsService.queryTestcaseCodeByTestcaseId(Integer.parseInt(testcaseId));
    }

//    @RequestMapping("/TestSubmit")
//    @ResponseBody
//    public String testSubmit() {
//
//        String suspiciousId = "146";
//        String testcaseId = "12419";
//        String assignee = "tianyang";
//        String simplifiedCase = "Hello world!";
//
//        // 向SuspiciousResults的对应记录中填充assignee信息，将其视为分析过的
//        SuspiciousResults suspiciousResult = suspiciousResultsService.querySuspiciousResultsById(Integer.parseInt(suspiciousId));
//        int updateAssigneeResult = 0;
//        if (suspiciousResult != null) {
//            suspiciousResult.setAssignee(assignee);
//            updateAssigneeResult = suspiciousResultsService.updateSuspiciousResults(suspiciousResult);
//        }
//
//        // 向Testcases的对应记录中填充simplifiedCase信息
//        Testcase testcase = testcaseService.queryTestcaseById(Integer.parseInt(testcaseId));
//        int updateTestcaseResult = 0;
//        if (testcase != null) {
//            testcase.setSimplified_case(simplifiedCase);
//            updateTestcaseResult = testcaseService.updateTestcase(testcase);
//        }
//
//        // 假如两个操作都成功了，才返回 "1"，表示提交成功
//        String flag = "0";
//        if (updateAssigneeResult == 1 && updateTestcaseResult == 1) {
//            flag = "1";
//        }
//
//        System.out.println("返回值为:" + flag);
//        return flag;
//    }
}
