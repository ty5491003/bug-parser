package com.ty.bugparser.service;

import com.ty.bugparser.pojo.SuspiciousResults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SuspiciousResultsService {

    List<SuspiciousResults> queryAllSuspiciousResults();

    List<SuspiciousResults> queryAllAnalysed();

    List<SuspiciousResults> queryAllNoAnalysed();

    int queryARandomNoAnalysedId();

    int queryAOutputIdBySuspiciousId(int SuspiciousId);

//    Map<String, Object> queryATestcase();
    String queryATestcaseByOutputId(int id);

    int updateSuspiciousResults(SuspiciousResults suspiciousResults);

}
