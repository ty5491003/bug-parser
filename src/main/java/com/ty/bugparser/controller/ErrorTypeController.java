package com.ty.bugparser.controller;

import com.alibaba.fastjson.JSON;
import com.ty.bugparser.pojo.ErrorType;
import com.ty.bugparser.pojo.SuspiciousResults;
import com.ty.bugparser.service.ErrorTypeService;
import com.ty.bugparser.service.ErrorTypeServiceImpl;
import lombok.Data;
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
@RequestMapping("/ErrorType")
@Data
public class ErrorTypeController {

    @Autowired
    private ErrorTypeService errorTypeService = new ErrorTypeServiceImpl();

    @RequestMapping("/queryAllErrorType")
    @ResponseBody
    public String queryAllErrorType() {
        List<ErrorType> errorTypes = errorTypeService.queryAllErrorType();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", errorTypes.size());
        map.put("data", errorTypes);

        return JSON.toJSONString(map);
    }
}
