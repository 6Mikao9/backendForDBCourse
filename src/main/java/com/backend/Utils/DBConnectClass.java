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
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection(url, username, password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables() throws SQLException {
        Statement stmt = con.createStatement();
        //创建用户表
        // user(userId, username, password, balance)
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "userId INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "password VARCHAR(50) NOT NULL, " +
                "balance DOUBLE NOT NULL)";
        stmt.executeUpdate(sql);
        //向用户表中插入一条示例数据
//        sql = "INSERT INTO users (id,username,userpassword,Sbalance) VALUES (?,?,?,?)";
//        PreparedStatement pstmt = con.prepareStatement(sql);
//        pstmt.setInt(1, 1);
//        pstmt.setString(2, "test");
//        pstmt.setString(3, "test");
//        pstmt.setDouble(4, 10000);
//        pstmt.executeUpdate();


        //创建开发者表
        // developers(developerId, developername, password, heat, balance)
        sql = "CREATE TABLE IF NOT EXISTS developers (" +
                "developerId INT AUTO_INCREMENT PRIMARY KEY, " +
                "developername VARCHAR(50) NOT NULL, " +
                "password VARCHAR(50) NOT NULL, " +
                "heat INT NOT NULL," +
                "balance DOUBLE NOT NULL)";
        stmt.executeUpdate(sql);

        // 创建mods表
        // mods(modId, softwareId, userId, downloads, heat)
        sql = "CREATE TABLE IF NOT EXISTS mods (" +
                "modId INT AUTO_INCREMENT PRIMARY KEY, " +
                "softwareId INT NOT NULL, " +
                "userId INT NOT NULL, " +
                "downloads INT NOT NULL, " +
                "heat INT NOT NULL)";
        stmt.executeUpdate(sql);

        // 创建softwares表
        // softwares(softwareId, softwarename, developerId, downloads, heat)
        sql = "CREATE TABLE IF NOT EXISTS softwares(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "softwareName VARCHAR(50) NOT NULL," +
                "developerId INT NOT NULL," +
                "downloads INT NOT NULL, " +
                "heat INT NOT NULL)";
        stmt.executeUpdate(sql);
    }

    // user登陆
    public static boolean userLogin(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    //developer登陆
    public static boolean developerLogin(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM developers WHERE developername='" + username + "' AND password='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    //用户注册
    public static void userRegister(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO users (userId,username,password,balance) VALUES (?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, 0);
        pstmt.setString(2, username);
        pstmt.setString(3, password);
        pstmt.setDouble(4, 10000);
        pstmt.executeUpdate();
    }

    public static ArrayList<Integer> searchMod(String keyword) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM mods WHERE keyword='" + keyword + "'";
        ResultSet rs = stmt.executeQuery(sql);

        return null;
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