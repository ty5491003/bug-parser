package com.ty.bugparser.service;

import com.ty.bugparser.pojo.SuspiciousResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SuspiciousResultsService {

    List<SuspiciousResults> queryAllSuspiciousResults();

    int updateSuspiciousResults(SuspiciousResults suspiciousResults);

}
