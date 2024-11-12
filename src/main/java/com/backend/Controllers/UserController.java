package com.backend.Controllers;

import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    // 文件存储路径
    private final Path rootLocation = Paths.get("mods");

    @PostMapping("/upload_mod")
    public ResponseEntity<Map<String, String>> uploadMod(@RequestParam("uploader_id") int userId, @RequestParam("file_name") String modname, @RequestParam("file") MultipartFile file, @RequestParam("softwarename") String softwarename) throws SQLException, IOException {
        Map<String, String> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }

        // 不允许modname为空
        if (modname.isEmpty()){
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }

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

//    @PostMapping
//    public ResponseEntity<Map<String, String>> uploadSoftware(@RequestParam("uploader_id"))

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, String> requestBody) throws SQLException {
        String keyword = requestBody.get("keyword");

        Map<String, ArrayList> result = new HashMap<>();

        ArrayList<Map<String, String>> softwaresList = searchSoftware(keyword);
        ArrayList<Map<String, String>> modsList = searchMod(keyword);

        result.put("softwares", softwaresList);
        result.put("mods", modsList);
        return ResponseEntity.ok(result);
    }
}
