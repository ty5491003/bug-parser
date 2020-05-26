package com.ty.bugparser.service;

import java.io.*;
import java.util.UUID;

public class ExecutorImpl {

    /**
     * 将代码写入随机名称的临时文件，并返回该文件名
     * @return
     */
    public String writeInFile(String code) {
        String testcaseFileName = "/export/nisl/ty/temp/" + UUID.randomUUID().toString().substring(0, 8) + ".js";
        FileWriter writer = null;
        try {
            writer = new FileWriter(testcaseFileName);
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testcaseFileName;
    }

    /**
     * 构建执行测试脚本的命令行指令
     * @param testcaseFileName 测试用例的临时文件名
     * @return 命令行指令
     */
    public String constructCmd(String testcaseFileName) {
        // 指定运行脚本，以及cmd指令
        String bashScriptFileName = "/export/nisl/yhy/javascriptFuzzingOther/test/ty.sh";
        return "bash" + " " + bashScriptFileName + " " + testcaseFileName;
    }

    public String executeScript(String prefix, String bashFileName, String testcaseFileName, File dir) throws Exception{

        // 构建缓冲区
        StringBuffer result = null;

        try {
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            result = new StringBuffer();

            ProcessBuilder pb = new ProcessBuilder(prefix, bashFileName, testcaseFileName);
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                result.append(s).append("\n");
            }
//            proc.waitFor();
            stdInput.close();

        } catch (IOException e) {
                e.printStackTrace();
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
	            // nothing
            }
        }
    }

}
