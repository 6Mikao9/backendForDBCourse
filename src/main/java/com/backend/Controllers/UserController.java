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
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();
        // 数据库中查询login
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        if(userLogin(username,password)) {
            map.put("token", "user");
        }else if(developerLogin(username,password)) {
            map.put("token", "developer");
        }else {
            map.put("token", "error");
            return ResponseEntity.status(401).body(map);
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        map.put("token", "user");
        DBConnectClass.userRegister(username,password);
        System.out.println(requestBody);
        return ResponseEntity.ok(map);
    }






}
