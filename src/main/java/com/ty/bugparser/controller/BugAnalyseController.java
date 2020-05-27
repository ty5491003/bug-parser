package com.ty.bugparser.controller;

import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.service.SuspiciousResultsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
