package com.ty.bugparser.controller;

import com.ty.bugparser.dao.SearchDao;
import com.ty.bugparser.dao.SearchDaoImpl;
import com.ty.bugparser.service.ExecutorImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

@Controller
@Slf4j
public class HelloWorld {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        log.warn("走到hello了！！！");
        return "Hello, Springboot!";
    }

    @RequestMapping("/index")
    public String toHtml() {
        return "index";
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

        // 以下为测试环境
//        ExecutorImpl executor = new ExecutorImpl();
//        String cmd = "C:/临时/5-25/ty.bat";
//        String testcaseFileName = "123";

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

    @RequestMapping("/search")
    @ResponseBody
    public String search(String dbPath) {
        log.warn("收到的dbPath参数为:" + dbPath);
        // 从dbPath中查询所有可疑的id号
        SearchDao searchDao = new SearchDaoImpl();
        List<String> allNeedAnalyseIds = searchDao.getAllNeedAnalyseIds(dbPath);
        log.warn("查询到的allNeedAnalyseIds结果为"+allNeedAnalyseIds);
        // 将数组的id转化为字符串，以便放入textarea
        StringBuilder ids = new StringBuilder();
        for (String aId : allNeedAnalyseIds) {
            ids.append(aId).append("\n");
        }

        return ids.toString();
    }

    @RequestMapping("/get")
    @ResponseBody
    public String get(String dbPath, String id) {

        // 从dbPath中，根据id查询对应的测试用例
        SearchDao searchDao = new SearchDaoImpl();
        return searchDao.getTestcaseById(dbPath, Integer.parseInt(id));
    }
}
