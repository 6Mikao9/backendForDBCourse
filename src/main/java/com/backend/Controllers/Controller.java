package com.backend.Controllers;

import com.backend.Utils.*;
import com.backend.service.*;
import com.backend.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.sql.*;
import java.util.*;

import static com.backend.Utils.DBConnectClass.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class Controller {

    @Autowired
    private TrainingDataService trainingDataService;

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
            //map.put("token", "error");
            return ResponseEntity.status(401).body(map);
        }
        System.out.println(requestBody);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/upload-data")
    public ResponseEntity<Map<String,String>> upload(@RequestParam("file") MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        map.put("data_id", "12345");
        return ResponseEntity.ok(map);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // 保存文件到服务器上的某个位置
            byte[] bytes = file.getBytes();
            File targetFile = new File("C:\\Users\\mille\\Desktop\\后端代码及接口文档\\backend\\files\\" + file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                fos.write(bytes);
            }

            // 调用解压缩方法
            boolean isUnzipSuccessful = unzipFile(targetFile);
            if (isUnzipSuccessful) {
                return ResponseEntity.ok("File uploaded and unzipped successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to unzip the file");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Could not upload the file: " + e.getMessage());
        }
    }

    private boolean unzipFile(File zipFile) {
        try (ZipFile zip = new ZipFile(zipFile)) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            File targetDir = new File("C:\\Users\\mille\\Desktop\\后端代码及接口文档\\backend\\files");
            if (!targetDir.exists()) {
                targetDir.mkdir();
            }

            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                File targetFile = new File(targetDir, entry.getName());

                if (entry.isDirectory()) {
                    targetFile.mkdirs();
                    continue;
                }

                // 确保目录存在
                targetFile.getParentFile().mkdirs();

                try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                    zip.getInputStream(entry).transferTo(fos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @PostMapping("/train/all")
    public ResponseEntity<Map<String, String>> trainAll(@RequestBody Map<String, String> requestBody) {
        String dataId = requestBody.get("data_id");
        log.info("trainAll:data_id = {}", dataId);
        String comment = requestBody.get("comment");
        log.info("trainAll:comment = {}", comment);

        // 这里应该添加一键训练的逻辑
        // 假设处理成功并返回了train_id
        String trainId = "12345";

        Map<String, String> response = new HashMap<>();
        response.put("train_id", trainId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/train/status/{train_id}")
    public ResponseEntity<Map<String, String>> getTrainStatus(@PathVariable String train_id) {
        // 调用服务层获取训练状态
        String status = "processing";
        Map<String, String> response = new HashMap<>();
        response.put("status", status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/result/preview/{trainId}")
    public ResponseEntity<ResultPreviewResponseData> previewResult(@PathVariable String trainId) {
        try {
            DBConnectClass.test();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultPreviewResponseData result = ResultPreviewResponseData.getPreviewResultExample();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<TrainHistoryQueryResponseData> queryHistory() {

        return ResponseEntity.ok(TrainHistoryQueryResponseData
                .getTrainHistoryQueryResponseDataExample());
    }

    @PostMapping("/query")
    public ResponseEntity<BundleResponseData> queryBundle(@RequestBody QueryRequest request) {
        try {
            // 这里应该是调用服务层或业务逻辑层来处理请求
            // 这里我们直接模拟返回结果
            log.info("queryBundle:request = {}", request);
            BundleResponseData response = new BundleResponseData();
            Bundle bundle = new Bundle();
            response.setBundle(bundle);
            bundle.setTicket(new String[]{"*****", "*****"});
            bundle.setHotel(new String[]{"*****", "*****"});
            bundle.setMeal(new String[]{"*****", "*****"});
            // 返回成功响应
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // 处理异常，这里只是简单返回500错误
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
