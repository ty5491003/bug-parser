package com.ty.bugparser.controller;

import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.service.ExecutorImpl;
import com.ty.bugparser.service.SuspiciousResultsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/BugAnalyse")
public class BugAnalyseController {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

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
        return suspiciousResultsService.queryATestcase();
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
}
