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

    /**
     * 执行命令行指令并捕获结果
     * @param cmd 命令行指令
     * @param dir 执行的目录，默认为 null
     * @return 捕获的结果字符串
     * @throws Exception
     */
    public String executeScript(String cmd, File dir) throws Exception{

        // 构建缓冲区
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
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
