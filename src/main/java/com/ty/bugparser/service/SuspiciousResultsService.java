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

//    Map<String, Object> queryATestcase();
    String queryATestcase();

    int updateSuspiciousResults(SuspiciousResults suspiciousResults);

}
