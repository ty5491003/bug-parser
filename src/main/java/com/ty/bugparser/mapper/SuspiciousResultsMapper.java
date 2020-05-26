package com.ty.bugparser.mapper;

import com.ty.bugparser.pojo.SuspiciousResults;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SuspiciousResultsMapper {

    List<SuspiciousResults> queryAllSuspiciousResults();

    int updateSuspiciousResults(SuspiciousResults suspiciousResults);

}
