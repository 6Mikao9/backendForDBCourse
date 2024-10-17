package com.backend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.sql.*;

@Configuration
public class CorsConfig {
    private static final long MAX_AGE = 24 * 60 * 60;
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * @author steven
     * @date 2019/12/30
     * @desc
     */
    public static class gaussdb {
        public static Connection GetConnection(String username, String passwd) {
            //驱动类。
            String driver = "com.huawei.gauss.jdbc.ZenithDriver";
            //数据库连接描述符。
            String sourceURL = "jdbc:zenith:@192.168.0.2";
            Connection conn = null;
            try {
                //加载数据库驱动。
                Class.forName(driver).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            try {
                //创建数据库连接。
                //getConnection(String url, String user, String password)
                conn = DriverManager.getConnection(sourceURL, username, passwd);
                System.out.println("Connection succeed!");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return conn;
        };
        //执行查询SQL语句。
        public static void SelectTest(Connection conn) {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                //执行SELECT语句。
                ResultSet rs = stmt.executeQuery("select * from steven_test");
                while (rs.next()) {
                    System.out.println("id:" + rs.getString(1) + ", c_name:" + rs.getString(2) + ", name:" + rs.getString(3));
                }

                stmt.close();
            } catch (SQLException e) {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
        public static void main(String[] args) {
            String userName = "steven";
            String password = "modb123$";
            //创建数据库连接。
            Connection conn = GetConnection(userName, password);
            //查询测试表。
            SelectTest(conn);
            //关闭数据库连接。
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
