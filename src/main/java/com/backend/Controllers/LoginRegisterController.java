package com.backend.Controllers;

import com.backend.Utils.DBConnectClass;
import com.backend.service.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        if (type.equals("user")) {
            if (userRegister(username, password)) {
                map.put("result", "SUC");
                return ResponseEntity.ok(map);
            } else {
                map.put("result", "FAIL");
                return ResponseEntity.status(414).body(map);
            }
        } else if (type.equals("developer")) {
            if (developerRegister(username, password)) {
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

    @GetMapping("/preview")
    public ResponseEntity<?> preview() throws SQLException {
        ArrayList<Map<String, Object>> arrayList = DBConnectClass.previewSoftwares();
        Map<String, ArrayList> map = new HashMap<>();
        map.put("preview", arrayList);
        return ResponseEntity.ok(map);
    }

    @GetMapping("software_images/{software_id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("software_id") int softwareId) throws SQLException, IOException {
        String softwarename = DBConnectClass.searchSoftwarenameById(softwareId);
        Path rootLocation = Paths.get("images");
        Path imagePath = rootLocation.resolve(softwarename + ".jpg");
        // 从资源目录中加载图片
        Resource resource = new UrlResource(imagePath.toUri());

        Path path = Paths.get(resource.getURI());
        byte[] imageData = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpg"); // 根据图片类型修改，例如 image/jpeg

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

    }
}
