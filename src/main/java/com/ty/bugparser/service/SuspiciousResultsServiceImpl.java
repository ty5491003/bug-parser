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
    public int updateSuspiciousResults(SuspiciousResults suspiciousResults) {
        return suspiciousResultsMapper.updateSuspiciousResults(suspiciousResults);
    }
}
