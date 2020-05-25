package com.ty.bugparser.dao;

import java.util.List;

public class SearchDaoImpl implements SearchDao {

    @Override
    public List<String> getAllNeedAnalyseIds(String dbPath) {
        DaoUtils daoUtils = new DaoUtils(dbPath);
        return daoUtils.search();
    }

    @Override
    public String getTestcaseById(String dbPath, int id) {
        DaoUtils daoUtils = new DaoUtils(dbPath);
        return daoUtils.get(id);
    }
}
