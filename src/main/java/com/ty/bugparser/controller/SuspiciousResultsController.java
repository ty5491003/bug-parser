package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.service.SuspiciousResultsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Slf4j
@RequestMapping("/SuspiciousResults")
public class SuspiciousResultsController {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

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
}
