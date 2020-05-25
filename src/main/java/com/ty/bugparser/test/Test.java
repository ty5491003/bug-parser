package com.ty.bugparser.test;

import com.ty.bugparser.dao.SearchDaoImpl;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        SearchDaoImpl searchDao = new SearchDaoImpl();
        String dbPath = "C:\\Users\\ty\\Downloads\\result_5_20.db";
        int id = 679;
        List<String> allNeedAnalyseIds = searchDao.getAllNeedAnalyseIds(dbPath);
        System.out.println(allNeedAnalyseIds);
        String code = searchDao.getTestcaseById(dbPath, id);
        System.out.println(code);
    }
}
