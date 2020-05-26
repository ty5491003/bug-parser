package com.ty.bugparser.dao;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DaoUtils {
    private static final String CLASS_NAME = "org.sqlite.JDBC";
    private static String DB_URL = null;

    public DaoUtils(String dbPath) {
        DB_URL = "jdbc:sqlite:" + dbPath;
    }

    public List<String> search() {
        // load the sqlite-JDBC driver using the current class loader
        Connection connection = null;
        List<String> ids = null;
        ResultSet rs = null;
        try {
            connection = createConnection();
            log.warn("Build search connection success.");
            Statement statement = connection.createStatement();
            String sql = "SELECT output_id FROM SuspiciousResults";
            // 执行查询语句
            log.warn("将执行的sql语句是:" + sql);
            rs = statement.executeQuery(sql);
            log.warn("Search finished!");
            ids = new ArrayList<>();
            while (rs.next()) {
                Object object = rs.getObject(1);
                ids.add(object.toString());
            }
        } catch (SQLException e) {
            log.warn("遇到了SQLException：");
            log.warn(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
                log.warn("数据库连接关闭时出现异常");
            }
        }
        return ids;
    }

    public String get(int id) {
        // load the sqlite-JDBC driver using the current class loader
        Connection connection = null;
        String code = null;
        ResultSet rs = null;
        try {
            connection = createConnection();
            log.warn("Build get connection success.");
            Statement statement = connection.createStatement();
            String sql = "SELECT testcase FROM Testcases WHERE Testcases.id = (SELECT testcase FROM HarnessResults WHERE id=" + id + ")";
            // 执行查询语句
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                code = rs.getString(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        return code;
    }

    // 创建Sqlite数据库连接
    public Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(CLASS_NAME);
        log.warn("Building Connection. The DB_URL is:" + DB_URL);
        return DriverManager.getConnection(DB_URL);
    }

}