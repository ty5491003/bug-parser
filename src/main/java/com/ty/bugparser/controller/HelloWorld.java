package com.ty.bugparser.controller;

import com.ty.bugparser.dao.SearchDao;
import com.ty.bugparser.dao.SearchDaoImpl;
import com.ty.bugparser.service.ExecutorImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HelloWorld {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
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
        ExecutorImpl executor = new ExecutorImpl();
        String testcaseFileName = executor.writeInFile(code);
        String cmd = executor.constructCmd(testcaseFileName);

        // 以下为生产环境
//        ExecutorImpl executor = new ExecutorImpl();
//        String cmd = "java -version";

        String result = "";
        try {
            result = executor.executeScript(cmd, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping("/search")
    @ResponseBody
    public String search(String dbPath) {

        // 从dbPath中查询所有可疑的id号
        SearchDao searchDao = new SearchDaoImpl();
        List<String> allNeedAnalyseIds = searchDao.getAllNeedAnalyseIds(dbPath);

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
