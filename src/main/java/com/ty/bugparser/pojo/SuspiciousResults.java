package com.ty.bugparser.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuspiciousResults {
    private int id;
    private String bugType;
    private String engine;
    private int output_id;
    private int bugLabel;
    private String reason;
    private String remarks;
    private String assignee;
}
