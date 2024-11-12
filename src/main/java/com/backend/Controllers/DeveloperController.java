package com.backend.Controllers;

import com.backend.Utils.DBConnectClass;
import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/developer")
public class DeveloperController {
    @Autowired
    TrainingDataService trainingDataService;

    // 文件存储路径
    private final Path rootLocation = Paths.get("softwares");

    @PostMapping("/upload_software")
    public ResponseEntity<Map<String, String>> uploadSoftware(@RequestParam("uploader_id") int developerId, @RequestParam("file_name") String softwarename, @RequestParam("file") MultipartFile file) throws SQLException, IOException {
        Map<String, String> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }

        if (softwarename.isEmpty()){
            map.put("result", "FAIL");
            return ResponseEntity.status(414).body(map);
        }

        Files.createDirectories(rootLocation);
        Path destinationFile = rootLocation.resolve(Paths.get(softwarename));
        file.transferTo(destinationFile);

        DBConnectClass.developerUploadSoftware(developerId, softwarename, destinationFile.toString());
        map.put("result", "SUC");
        return ResponseEntity.ok(map);
    }

}
