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


    // 将db.properties文件读取出来
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
        // mods(modId, modname, softwareId, userId, price, downloads, heat, filepath)
        sql = "CREATE TABLE IF NOT EXISTS mods (" +
                "modId INT AUTO_INCREMENT PRIMARY KEY, " +
                "modname VARCHAR(50) NOT NULL ," +
                "softwareId INT NOT NULL, " +
                "userId INT NOT NULL, " +
                "price INT NOT NULL," +
                "downloads INT NOT NULL, " +
                "heat INT NOT NULL, " +
                "filepath VARCHAR(100) NOT NULL)";
        stmt.executeUpdate(sql);

        // 创建softwares表
        // softwares(softwareId, softwarename, developerId, price, downloads, heat, filepath)
        sql = "CREATE TABLE IF NOT EXISTS softwares(" +
                "softwareId INT AUTO_INCREMENT PRIMARY KEY," +
                "softwareName VARCHAR(50) NOT NULL," +
                "developerId INT NOT NULL," +
                "price INT NOT NULL," +
                "downloads INT NOT NULL, " +
                "heat INT NOT NULL, " +
                "filepath VARCHAR(100) NOT NULL)";
        stmt.executeUpdate(sql);

        // 创建carts表
        // cart(userId, softwareId)
        sql = "CREATE TABLE IF NOT EXISTS carts(" +
                "userId INT ," +
                "softwareId INT ," +
                "PRIMARY KEY (userId, softwareId) )";
        stmt.executeUpdate(sql);

        // 创建message表
        // message(messageId, sendId, receiveId, content)
        sql = "CREATE TABLE IF NOT EXISTS messages(" +
                "messageId INT AUTO_INCREMENT PRIMARY KEY," +
                "sendId INT NOT NULL," +
                "receiveId INT NOT NULL," +
                "content VARCHAR(500) NOT NULL)";
        stmt.executeUpdate(sql);
    }

    // user登陆
    public static int userLogin(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT userId FROM users WHERE username='" + username + "' AND password='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        Integer userId = -1;
        if (rs.next()) {
            userId = rs.getInt("userId");
        }
        return userId;
    }

    //developer登陆
    public static int developerLogin(String username, String password) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "SELECT developerId FROM developers WHERE developername='" + username + "' AND password='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        // 如果返回-1，则意味着在develoers表中没有找到对应的name和password
        Integer developerId = -1;
        if (rs.next()) {
            developerId = rs.getInt("developerId");
        }
        return developerId;
    }

    //用户注册
    public static boolean userRegister(String username, String password, String confirm) throws SQLException {
        // 如果password与confirm不相同
        if (!password.equals(confirm)) {
            return false;
        }

        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM users WHERE username = '" + username + "'";
        ResultSet rs = stmt.executeQuery(sql);

        // 如果已经有相同的用户名
        if (rs.next()) {
            return false;
        }

        sql = "INSERT INTO users (userId,username,password,balance) VALUES (?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, 0);
        pstmt.setString(2, username);
        pstmt.setString(3, password);
        // 默认新注册用户的余额balance为0
        pstmt.setDouble(4, 0);
        pstmt.executeUpdate();
        return true;
    }

    public static boolean developerRegister(String developername, String password, String confirm) throws SQLException {
        if (!password.equals(confirm)) {
            return false;
        }

        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM developers WHERE developername = '" + developername + "'";
        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()) {
            return false;
        }

        sql = "INSERT INTO developers (developerId, developername, password, heat, balance) VALUES (?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        // developerId会自动递增
        pstmt.setInt(1, 0);
        pstmt.setString(2, developername);
        pstmt.setString(3, password);
        // 默认新注册的developer的heat和balance均为0
        pstmt.setInt(4, 0);
        pstmt.setInt(5, 0);
        pstmt.executeUpdate();
        return true;
    }

    // 每个mod都会唯一对应一个software
    // 一个mod可以多次上传，而且会被分配不同的modId
    public static boolean userUploadMod(int userId, String modname, String path, String softwarename) throws SQLException {
        String sql = "SELECT softwareId FROM softwares WHERE softwarename = '" + softwarename + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        int softwareId;
        if (rs.next()) {
            softwareId = rs.getInt("softwareId");
        }
        // mod对应的软件不存在
        else {
            return false;
        }

        sql = "INSERT INTO mods (modId, modname, softwareId, userId, price, downloads, heat, filepath) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        // modId会自动升序
        pstmt.setInt(1, 0);
        pstmt.setString(2, modname);
        pstmt.setInt(3, softwareId);
        pstmt.setInt(4, userId);
        // 对于新上传的mod，downloads和heat均设置为0
        pstmt.setInt(5, 0);
        pstmt.setInt(6, 0);
        pstmt.setInt(7, 0);
        pstmt.setString(8, path);

        pstmt.executeUpdate();
        return true;
    }

    public static boolean developerUploadSoftware(int developerId, String softwarename, String path) throws SQLException {
        String sql = "INSERT INTO softwares (softwareId, softwarename, developerId, price, downloads, heat, filepath) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, 0);
        pstmt.setString(2, softwarename);
        pstmt.setInt(3, developerId);
        pstmt.setInt(4, 0);
        pstmt.setInt(5, 0);
        pstmt.setInt(6, 0);
        ;
        pstmt.setString(7, path);
        pstmt.executeUpdate();
        return true;
    }

    public static ArrayList<Map<String, String>> searchModsByName(String keyword) throws SQLException {
        ArrayList<Map<String, String>> results = new ArrayList<>();
        String sql = "SELECT modId FROM mods WHERE modname='" + keyword + "'";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Integer modId = rs.getInt("modId");
            Map map = new HashMap();
            map.put("id", modId.toString());
            results.add(map);
        }
        return results;
    }

    public static ArrayList<Map<String, String>> searchSoftwaresByName(String keyword) throws SQLException {
        ArrayList<Map<String, String>> results = new ArrayList<>();
        String sql = "SELECT softwareId FROM softwares WHERE softwarename='" + keyword + "'";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Integer softwareId = rs.getInt("softwareId");
            Map map = new HashMap();
            map.put("id", softwareId.toString());
            results.add(map);
        }
        return results;
    }

    public static boolean userAddCart(int userId, int softwareId) throws SQLException {
        String sql = "SELECT * FROM carts WHERE userId = " + userId + " AND softwareId =" + softwareId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        // 说明用户之前已经将该software加入到购物车中
        if (rs.next()) {
            return false;
        }

        sql = "INSERT INTO carts (userId, softwareId) VALUES (?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, softwareId);
        pstmt.executeUpdate();
        return true;
    }

    public static String searchModnameById(int modId) throws SQLException {
        String sql = "SELECT modname FROM mods WHERE modId = '" + modId + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String modname = "";
        if (rs.next()) {
            modname = rs.getString("modname");
        }
        return modname;
    }

    public static String searchSoftwarenameById(int softwareId) throws SQLException {
        String sql = "SELECT softwarename FROM softwares WHERE softwareId = '" + softwareId + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String softwarename = "";
        if (rs.next()) {
            softwarename = rs.getString("softwarename");
        }
        return softwarename;
    }

    public static int searchSoftwarePriceById(int softwareId) throws SQLException {
        String sql = "SELECT price FROM softwares WHERE softwareId = '" + softwareId + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        int price = -1;
        if (rs.next()) {
            price = rs.getInt("price");
        }
        return price;
    }

    public static String searchUsernameById(int userId) throws SQLException {
        String sql = "SELECT username FROM users WHERE userId = '" + userId + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String username = "";
        if (rs.next()) {
            username = rs.getString("username");
        }
        return username;
    }

    public static int searchUserBalanceById(int userId) throws SQLException {
        String sql = "SELECT balance FROM users WHERE userId = '" + userId + "'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        int balance = -1;
        if (rs.next()) {
            balance = rs.getInt("balance");
        }
        return balance;
    }

    public static ArrayList<Map<String, Object>> searchUserCartsById(int userId) throws SQLException {
        String sql = "SELECT softwareId FROM carts WHERE userId = " + userId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<Integer> softwaresId = new ArrayList<>();
        while (rs.next()) {
            softwaresId.add(rs.getInt("softwareId"));
        }

        ArrayList<Map<String, Object>> carts = new ArrayList<>();
        for (int i = 0; i < softwaresId.size(); i++) {
            int softwareId = softwaresId.get(i);
            String softwarename = searchSoftwarenameById(softwareId);
            int price = searchSoftwarePriceById(softwareId);
            Map<String, Object> map = new HashMap();
            map.put("softwarename", softwarename);
            map.put("price", price);
            map.put("softwareId", softwareId);
            carts.add(map);
        }
        return carts;
    }

    public static String searchDevelopernameById(int developerId) throws SQLException {
        String sql = "SELECT developername FROM developers WHERE developerId = " + developerId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        String developername = "";
        if (rs.next()) {
            developername = rs.getString("developername");
        }
        return developername;
    }

    public static int searchDeveloperBalanceById(int developerId) throws SQLException {
        String sql = "SELECT balance FROM developers WHERE developerId = " + developerId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        int balance = -1;
        if (rs.next()) {
            balance = rs.getInt("balance");
        }
        return balance;
    }

    public static boolean sendMessage(int sendId, int receiveId, String content) throws SQLException {
        String sql = "SELECT * FROM users WHERE userId = " + sendId;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        // 如果没有找到发送者
        if (!rs.next()) {
            return false;
        }

        sql = "SELECT * FROM users WHERE userId = " + receiveId;
        rs = stmt.executeQuery(sql);
        // 如果没有找到接收者
        if (!rs.next()) {
            return false;
        }

        sql = "INSERT INTO messages (messageId, sendId, receiveId, content) VALUES (?,?,?,?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        // messageId会自动递增
        pstmt.setInt(1,0);
        pstmt.setInt(2, sendId);
        pstmt.setInt(3, receiveId);
        pstmt.setString(4, content);
        pstmt.executeUpdate();
        return true;
    }


    public static void test() throws SQLException {
        System.out.println("initialize");

        System.out.println("Database connected");

        //向用户表中插入一条示例数据
//        sql = "INSERT INTO users (id,username,userpassword,Sbalance) VALUES (?,?,?,?)";
//        PreparedStatement pstmt = con.prepareStatement(sql);
//        pstmt.setInt(1, 1);
//        pstmt.setString(2, "test");
//        pstmt.setString(3, "test");
//        pstmt.setDouble(4, 10000);
//        pstmt.executeUpdate();

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