package com.ty.bugparser.test;

import java.sql.*;

public class OpSqliteDB {

    private static final String Class_Name = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\ty\\Downloads\\result_5_20.db";

    public static void main(String args[]) {
        // load the sqlite-JDBC driver using the current class loader
        Connection connection = null;
        try {
            connection = createConnection();
            func1(connection);
            System.out.println("Success!");
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
    }

    // 创建Sqlite数据库连接
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName(Class_Name);
        return DriverManager.getConnection(DB_URL);
    }

    public static void func1(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "SELECT output_id FROM SuspiciousResults";

        // 执行查询语句
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            Object object = rs.getObject(1);
            System.out.println(object.toString());
        }
    }
}