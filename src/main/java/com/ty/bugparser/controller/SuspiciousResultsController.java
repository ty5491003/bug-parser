package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.service.SuspiciousResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class SuspiciousResultsController {

    @Autowired
    private SuspiciousResultsService suspiciousResultsService;

    /**
     * 数据请求接口，将返回请求的数据，与 list.html 对接
     *
     * @return
     */
    @RequestMapping("/SuspiciousResults/manage")
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
}
