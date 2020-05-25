package com.ty.bugparser.dao;

import java.util.List;

public interface SearchDao {

    List<String> getAllNeedAnalyseIds(String dbPath);

    String getTestcaseById(String dbPath, int id);

}
