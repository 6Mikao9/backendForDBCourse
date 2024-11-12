package com.backend.Controllers;

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
public class LoginRegisterController {

    @Autowired
    private TrainingDataService trainingDataService;

    // user和developer均可通过此接口登录
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
            return ResponseEntity.status(414).body(map);
        }
        return ResponseEntity.ok(map);
    }

    // 目前只支持用户注册
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();
        String type = requestBody.get("type");
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        String confirm = requestBody.get("confirm");

        if (type.equals("user")) {
            if (userRegister(username, password, confirm)) {
                map.put("result", "SUC");
                return ResponseEntity.ok(map);
            } else {
                map.put("result", "FAIL");
                return ResponseEntity.status(414).body(map);
            }
        } else if (type.equals("developer")){
            if (developerRegister(username, password, confirm)) {
                map.put("result", "SUC");
                return ResponseEntity.ok(map);
            } else {
                map.put("result", "FAIL");
                return ResponseEntity.status(414).body(map);
            }
        }
        // 如果type不对应，则失败
        else {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }



    }
}
