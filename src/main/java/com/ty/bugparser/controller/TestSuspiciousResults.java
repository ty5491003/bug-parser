package com.ty.bugparser.controller;

import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.service.SuspiciousResultsService;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestSuspiciousResults {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

    @RequestMapping("/test1")
    public List<SuspiciousResults> queryAll() {
        return suspiciousResultsService.queryAllSuspiciousResults();
    }

    @RequestMapping("/update1")
    public int update() {
        SuspiciousResults result = new SuspiciousResults(2, "Exception", "Rhino", 666, 3, "previous", "no", "tianyang");
        return suspiciousResultsService.updateSuspiciousResults(result);

    }
}
