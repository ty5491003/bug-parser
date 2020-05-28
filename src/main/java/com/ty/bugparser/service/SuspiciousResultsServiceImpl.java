package com.ty.bugparser.service;

import com.ty.bugparser.mapper.SuspiciousResultsMapper;
import com.ty.bugparser.pojo.SuspiciousResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspiciousResultsServiceImpl implements SuspiciousResultsService{

    @Autowired
    private SuspiciousResultsMapper suspiciousResultsMapper;

    @Override
    public List<SuspiciousResults> queryAllSuspiciousResults() {
        return suspiciousResultsMapper.queryAllSuspiciousResults();
    }

    @Override
    public List<SuspiciousResults> queryAllAnalysed() {
        return suspiciousResultsMapper.queryAllAnalysed();
    }

    @Override
    public List<SuspiciousResults> queryAllNoAnalysed() {
        return suspiciousResultsMapper.queryAllNoAnalysed();
    }

    @Override
    public int queryRandomNoAnalysedSuspiciousId() {
        return suspiciousResultsMapper.queryRandomNoAnalysedSuspiciousId();
    }

    @Override
    public int queryHarnessIdBySuspiciousId(int suspiciousId) {
        return suspiciousResultsMapper.queryHarnessIdBySuspiciousId(suspiciousId);
    }

    @Override
    public int queryTestcaseIdByHarnessId(int harnessId) {
        return suspiciousResultsMapper.queryTestcaseIdByHarnessId(harnessId);
    }

    @Override
    public String queryTestcaseCodeByTestcaseId(int testcaseId) {
        return suspiciousResultsMapper.queryTestcaseCodeByTestcaseId(testcaseId);
    }


    @Override
    public int updateSuspiciousResults(SuspiciousResults suspiciousResults) {
        return suspiciousResultsMapper.updateSuspiciousResults(suspiciousResults);
    }
}
