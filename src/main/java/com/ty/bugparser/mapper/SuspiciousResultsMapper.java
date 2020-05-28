package com.ty.bugparser.mapper;

import com.ty.bugparser.pojo.SuspiciousResults;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SuspiciousResultsMapper {

    List<SuspiciousResults> queryAllSuspiciousResults();

    List<SuspiciousResults> queryAllAnalysed();

    List<SuspiciousResults> queryAllNoAnalysed();

    int queryARandomNoAnalysedId();

    int queryAOutputIdBySuspiciousId(int SuspiciousId);

//    Map<String, Object> queryATestcase();
    String queryATestcaseByOutputId(int id);

    int updateSuspiciousResults(SuspiciousResults suspiciousResults);

}
