package com.backend.Controllers;

import com.backend.Utils.*;
import com.backend.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.sql.*;
import java.util.*;

import static com.backend.Utils.DBConnectClass.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class UserController {

    @Autowired
    private TrainingDataService trainingDataService;

    // user和developer均可通过此接口登录
    /*
    请求体参数
    username String
    password String

    响应
    type 用户类型
    userId 用户Id
     */
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();
        // 数据库中查询login
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        if (userLogin(username, password) != -1) {
            map.put("type", "user");
            map.put("userId", String.valueOf(userLogin(username, password)));
        } else if (developerLogin(username, password) != -1) {
            map.put("type", "developer");
            map.put("userId", String.valueOf(developerLogin(username, password)));
        } else {
            return ResponseEntity.status(401).body(map);
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        String confirm = requestBody.get("confirm");


        if (userRegister(username, password, confirm)) {
            map.put("result", "SUC");
            return ResponseEntity.ok(map);
        } else {
            map.put("result", "FAIL");
            return ResponseEntity.status(401).body(map);
        }
    }
}
