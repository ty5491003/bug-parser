package com.ty.bugparser.service;

import com.ty.bugparser.mapper.SuspiciousResultsMapper;
import com.ty.bugparser.pojo.SuspiciousResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public int queryARandomNoAnalysedId() {
        return suspiciousResultsMapper.queryARandomNoAnalysedId();
    }

    @Override
    public int queryAOutputIdBySuspiciousId(int SuspiciousId) {
        return suspiciousResultsMapper.queryAOutputIdBySuspiciousId(SuspiciousId);
    }

    @Override
    public String queryATestcaseByOutputId(int id) {
        return suspiciousResultsMapper.queryATestcaseByOutputId(id);
    }

    //    @Override
//    public Map<String, Object> queryATestcase() {
//        return suspiciousResultsMapper.queryATestcase();
//    }


    @Override
    public int updateSuspiciousResults(SuspiciousResults suspiciousResults) {
        return suspiciousResultsMapper.updateSuspiciousResults(suspiciousResults);
    }
}
