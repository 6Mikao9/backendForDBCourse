package com.backend.Controllers;

import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/user")
public class ModAndSoftwareController {
    @Autowired
    TrainingDataService trainingDataService;

    // 文件存储路径
    private final Path rootLocation = Paths.get("uploads");

    @PostMapping("/upload_mod")
    public ResponseEntity<?> uploadMod(@RequestParam("uploader_id")int userId, @RequestParam("file_name") String modname, @RequestParam("file") MultipartFile file) throws SQLException, IOException {
        if (file.isEmpty()){
            return ResponseEntity.status(400).body("文件不能为空");
        }
        System.out.println("AAA");
        Files.createDirectories(rootLocation);
        Path destinationFile = rootLocation.resolve(Paths.get(modname));
        file.transferTo(destinationFile);
        System.out.println("AAA");
        return ResponseEntity.ok("文件上传成功，保存路径："+destinationFile.toString());
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, ArrayList> result = new HashMap<>();

        String keyword = requestBody.get("keyword");
        ArrayList<Map<String, String>> softwaresList = searchSoftware(keyword);
        ArrayList<Map<String, String>> modsList = searchMod(keyword);

        result.put("softwares", softwaresList);
        result.put("mods", modsList);
        return ResponseEntity.ok(result);
    }
}
