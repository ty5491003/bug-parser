package com.ty.bugparser.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

@Data
@Component
@ConfigurationProperties("user.executor")
@AllArgsConstructor
@NoArgsConstructor
public class Executor {

    private String prefix;
    private String bashFilePath;
    private String tempFileDir;

    /**
     * 将代码写入指定目录下的随机名称的临时文件，并返回该文件名
     * @return 文件名（完整地址+文件名）
     */
    public String writeInFile(String code) {
        String testcaseFileName = tempFileDir + UUID.randomUUID().toString().substring(0, 8) + ".js";
        FileWriter writer;
        try {
            writer = new FileWriter(testcaseFileName);
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testcaseFileName;
    }

    public String executeScript(String code) {
        // 创建临时文件，将测试用例写入该文件
        String testcaseFileName = writeInFile(code);

        // 构建缓冲区
        ProcessBuilder pb;
        StringBuffer result = null;
        Process proc = null;
        BufferedReader stdInput = null;
        try {
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            result = new StringBuffer();

            pb = new ProcessBuilder(prefix, bashFilePath, testcaseFileName);
            pb.redirectErrorStream(true);
            proc = pb.start();
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                result.append(s).append("\n");
            }

            stdInput.close();

        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            // 销毁子进程
            if (proc != null) {
                proc.destroy();
            }
            // 关闭流
            closeStream(stdInput);
            // 删除临时文件
            File tempFile = new File(testcaseFileName);
            if (tempFile.exists() && tempFile.isFile()) {
                tempFile.delete();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception ignored) {
            }
        }
    }
}
