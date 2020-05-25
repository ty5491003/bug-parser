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
    public String run(String code,
                      String result,
                      String dbPath,
//                      String ids,
                      String id,
                      Model model) {
        System.out.println(code);
        // 以下为正式版
        ExecutorImpl executor = new ExecutorImpl();
        String testcaseFileName = executor.writeInFile(code);
        String cmd = executor.constructCmd(testcaseFileName);

        // 以下为测试版
//        ExecutorImpl executor = new ExecutorImpl();
//        String cmd = "java -version";

        String newResult = null;
        try {
            newResult = executor.executeScript(cmd, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 不变的
        model.addAttribute("id", id);
//        model.addAttribute("ids", ids);
        model.addAttribute("dbPath", dbPath);
        model.addAttribute("code", code);

        // 要变的
        model.addAttribute("result", newResult);

        return "index";
    }

    @RequestMapping("/search")
    public String search(String code,
                      String result,
                      String dbPath,
//                      String ids,
                      String id,
                      Model model) {

        // 从dbPath中查询所有可疑的id号
        SearchDao searchDao = new SearchDaoImpl();
        List<String> allNeedAnalyseIds = searchDao.getAllNeedAnalyseIds(dbPath);

        // 将数组的id转化为字符串，以便放入textarea
        StringBuilder newIds = new StringBuilder();
        for (String aId : allNeedAnalyseIds) {
            newIds.append(aId).append("\n");
        }

        // 不变的
        model.addAttribute("id", id);
        model.addAttribute("result", result);
        model.addAttribute("dbPath", dbPath);
        model.addAttribute("code", code);

        // 要变的
        model.addAttribute("ids", newIds.toString());

        return "index";
    }

    @RequestMapping("/get")
    public String get(String code,
                      String result,
                      String dbPath,
//                      String ids,
                      String id,
                      Model model) {

        // 从dbPath中，根据id查询对应的测试用例
        SearchDao searchDao = new SearchDaoImpl();
        String newCode = searchDao.getTestcaseById(dbPath, Integer.parseInt(id));
        System.out.println(newCode);
        // 不变的
        model.addAttribute("id", id);
        model.addAttribute("result", result);
        model.addAttribute("dbPath", dbPath);
//        model.addAttribute("ids", ids);

        // 要变的
        model.addAttribute("code", newCode);

        return "index";
    }
}
