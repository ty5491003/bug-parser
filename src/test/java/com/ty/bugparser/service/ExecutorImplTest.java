package com.ty.bugparser.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutorImplTest {


    @Test
    public void executeScriptTest() throws Exception {
        ExecutorImpl executor = new ExecutorImpl();
        String result = executor.executeScript("java -version", null);
        System.out.println(result);
    }
}