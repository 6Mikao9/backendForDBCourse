package com.backend.Utils;

import lombok.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DBConnectClass {

    private static String url;//在配置文件中修改成自己的数据库
    private static String username;
    private static String password;

    @Getter
    public static Connection con;


    //将db.properties文件读取出来
    static {
        try {
            Properties properties = new Properties();
            properties.load(DBConnectClass.class.getClassLoader().getResourceAsStream("db.properties"));
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection(url,username,password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables() throws SQLException {
        //创建用户表
        Statement stmt = con.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "userpassword VARCHAR(50) NOT NULL, " +
                "Sbalance DOUBLE NOT NULL)";
        stmt.executeUpdate(sql);
        //向用户表中插入一条示例数据
        sql = "INSERT INTO users (id,username,userpassword,Sbalance) VALUES (?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, 1);
        pstmt.setString(2, "test");
        pstmt.setString(3, "test");
        pstmt.setDouble(4, 10000);
        pstmt.executeUpdate();
        //创建开发者表
        sql = "CREATE TABLE IF NOT EXISTS developers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "userpassword VARCHAR(50) NOT NULL, " +
                "heat INT NOT NULL,"+
                "balance DOUBLE NOT NULL)";
        stmt.executeUpdate(sql);
    }

    /**
     * 用户登录
     */
    public static boolean userLogin(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM users WHERE username='" + username + "' AND userpassword='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    //developer登陆
    public static boolean developerLogin(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM developers WHERE username='" + username + "' AND userpassword='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    //用户注册
    public static void userRegister(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO users (id,username,userpassword,Sbalance) VALUES (?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, 0);
        pstmt.setString(2, username);
        pstmt.setString(3, password);
        pstmt.setDouble(4, 10000);
        pstmt.executeUpdate();
    }

    public static void test() throws SQLException {
        System.out.println("initialize");

        System.out.println("Database connected");

        //使用示例

        Statement stmt = con.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "Sid INT AUTO_INCREMENT PRIMARY KEY, " +
                "Sname VARCHAR(50) NOT NULL, " +
                "Sgander VARCHAR(50) NOT NULL, " +
                "Sage INT NOT NULL)";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO students (Sid,Sname,Sgander,Sage) VALUES (?,?,?,?)";
//        PreparedStatement pstmt = con.prepareStatement(sql);
//        pstmt.setInt(1, 22371285);
//        pstmt.setString(2, "Yuntao Liu");
//        pstmt.setString(3, "man");
//        pstmt.setInt(4, 20);
//        pstmt.executeUpdate();

        sql = "SELECT * FROM students";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("Sid");
            String name = rs.getString("Sname");
            String sgander = rs.getString("Sgander");
            int age = rs.getInt("Sage");
            System.out.println(id + " " + name + " " + sgander + " " + age);
        }
        rs.close();
        //pstmt.close();
        stmt.close();

        con.close();
    }
}