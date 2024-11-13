package com.backend.Controllers;

import com.backend.Utils.DBConnectClass;
import com.backend.service.TrainingDataService;
import com.mongodb.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.backend.Utils.DBConnectClass.*;

@RestController
@RequestMapping("api/user")

public class UserController {
    @Autowired
    TrainingDataService trainingDataService;

    @PostMapping("/upload_mod")
    public ResponseEntity<Map<String, String>> uploadMod(@RequestParam("uploader_id") int userId, @RequestParam("file_name") String modname, @RequestParam("file") MultipartFile file, @RequestParam("softwarename") String softwarename) throws SQLException, IOException {
        Map<String, String> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }

        // 不允许modname为空
        if (modname.isEmpty()) {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }
        Path rootLocation = Paths.get("mods");
        Files.createDirectories(rootLocation);
        Path destinationFile = rootLocation.resolve(Paths.get(modname));
        file.transferTo(destinationFile);

        if (userUploadMod(userId, modname, destinationFile.toString(), softwarename)) {
            map.put("result", "SUC");
            return ResponseEntity.ok(map);
        } else {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, String> requestBody) throws SQLException {
        String keyword = requestBody.get("keyword");

        Map<String, ArrayList> result = new HashMap<>();

        ArrayList<Map<String, String>> softwaresList = searchSoftwaresByName(keyword);
        ArrayList<Map<String, String>> modsList = searchModsByName(keyword);

        result.put("softwares", softwaresList);
        result.put("mods", modsList);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addCart")
    public ResponseEntity<Map<String, String>> addCart(@RequestBody Map<String, Integer> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();

        int userId = requestBody.get("userId");
        int softwareId = requestBody.get("softwareId");

        if (DBConnectClass.userAddCart(userId, softwareId)) {
            map.put("result", "SUC");
            return ResponseEntity.ok(map);
        } else {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("type") String type, @RequestParam("id") int id) throws SQLException, MalformedURLException {

        if (type.equals("software")) {
            String softwarename = DBConnectClass.searchSoftwarenameById(id);
            if (softwarename.isEmpty()) {
                return ResponseEntity.status(414).build();
            }
            Path rootLocation = Paths.get("softwares");
            Path filepath = rootLocation.resolve(Paths.get(softwarename)).normalize();
            Resource resource = new UrlResource(filepath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok(resource);
            } else {
                return ResponseEntity.status(414).build();
            }
        } else if (type.equals("mod")) {
            String modname = DBConnectClass.searchModnameById(id);
            // modname为空，说明该modId不存在
            if (modname.isEmpty()) {
                return ResponseEntity.status(414).build();
            }
            Path rootLocation = Paths.get("mods");
            Path filepath = rootLocation.resolve(Paths.get(modname)).normalize();
            Resource resource = new UrlResource(filepath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok(resource);
            } else {
                return ResponseEntity.status(414).build();
            }
        } else {
            return ResponseEntity.status(414).build();
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(@RequestParam("userId") int userId) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        String username = DBConnectClass.searchUsernameById(userId);
        int balance = DBConnectClass.searchUserBalanceById(userId);
        // 说明未找到
        if (username.isEmpty() || balance == -1) {
            return ResponseEntity.notFound().build();
        }
        ArrayList<Map<String, Object>> carts = DBConnectClass.searchUserCartsById(userId);

        map.put("username", username);
        map.put("balance", balance);
        map.put("cart", carts);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buy(@RequestParam("userId") int userId, @RequestParam("softwareId") int softwareId) throws SQLException {
        if (!DBConnectClass.isUserById(userId) || !DBConnectClass.isSoftwareById(softwareId)) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> map = DBConnectClass.userBuy(userId, softwareId);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/lib/{uid}")
    public ResponseEntity<?> lib(@PathVariable("uid") int userId) throws SQLException {
        if (!DBConnectClass.isUserById(userId)){
            return ResponseEntity.notFound().build();
        }

        Map<String, ArrayList> map = new HashMap<>();

        ArrayList<Map> arrayList = DBConnectClass.libByUserId(userId);

        map.put("softwares", arrayList);
        return ResponseEntity.ok(map);
    }
}
