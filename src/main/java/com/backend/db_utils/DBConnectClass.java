package com.backend.db_utils;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DBConnectClass {

    private static String url;//在配置文件中修改成自己的数据库
    private static String username;
    private static String password;

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
            Connection con = DriverManager.getConnection(url,username,password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SQLException {
        System.out.println("initialize");
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        con = DriverManager.getConnection(url,username,password);
        System.out.println("Database connected");

        //使用示例
        /*
        Statement stmt = con.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS student (" +
                "Sid INT AUTO_INCREMENT PRIMARY KEY, " +
                "Sname VARCHAR(50) NOT NULL, " +
                "Sgander VARCHAR(50) NOT NULL, " +
                "Sage INT NOT NULL)";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO student (Sid,Sname,Sgander,Sage) VALUES (?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, 22371285);
        pstmt.setString(2, "Yuntao Liu");
        pstmt.setString(3, "man");
        pstmt.setInt(4, 20);
        pstmt.executeUpdate();

        sql = "SELECT * FROM student";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("Sid");
            String name = rs.getString("Sname");
            String sgander = rs.getString("Sgander");
            int age = rs.getInt("Sage");
            System.out.println(id + " " + name + " " + sgander + " " + age);
        }
        rs.close();
        pstmt.close();
        stmt.close();
         */
        //con.close();
    }
}