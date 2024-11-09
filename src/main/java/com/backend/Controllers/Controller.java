package com.backend.Controllers;

import com.backend.Utils.*;
import com.backend.service.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.nio.file.*;
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
        System.out.println("request body:"+requestBody);
        // 数据库中查询login
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        if(userLogin(username,password)) {
            map.put("type", "user");
        }else if(developerLogin(username,password)) {
            map.put("type", "developer");
        }else {
            //map.put("token", "error");
            return ResponseEntity.status(401).body(map);
        }
        map.put("id","1234567890");

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

    private static final String UPLOAD_DIR1 = "D:\\data\\test\\";

    @PostMapping("/developer/upload_software")
    public ResponseEntity<?> uploadSoftware(@RequestParam("uploader_id") String uploaderId,
                                       @RequestParam("file_name") String fileName,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("softwareName") String softwareName) {
        //打印请求参数
        System.out.println("uploaderId: " + uploaderId);
        System.out.println("fileName: " + fileName);
        System.out.println("softwareName: " + softwareName);

        // 检查文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请提供有效的文件");
        }

        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR1);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件到指定位置
            //System.out.println("fileName: " + fileName);
            //为文件名添加前缀和随机数，防止重复
            fileName = UUID.randomUUID() + "_" + fileName;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 这里可以添加更多的业务逻辑，例如将mod信息保存到数据库等

            return ResponseEntity.ok().body("{\"status\": \"success\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"status\": \"fail\", \"message\": \"上传过程中发生错误\"}");
        }
    }



    private static final String UPLOAD_DIR = "D:\\data\\test\\";

    @PostMapping("/user/upload_mod")
    public ResponseEntity<?> uploadMod(@RequestParam("uploader_id") String uploaderId,
                                       @RequestParam("file_name") String fileName,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("softwareName") String softwareName) {
        //打印请求参数
        System.out.println("uploaderId: " + uploaderId);
        System.out.println("fileName: " + fileName);
        System.out.println("softwareName: " + softwareName);

        // 检查文件是否为空
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请提供有效的文件");
        }

        try {
            // 确保上传目录存在
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件到指定位置
            //System.out.println("fileName: " + fileName);
            //为文件名添加前缀和随机数，防止重复
            fileName = UUID.randomUUID() + "_" + fileName;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 这里可以添加更多的业务逻辑，例如将mod信息保存到数据库等

            return ResponseEntity.ok().body("{\"status\": \"success\"}");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"status\": \"fail\", \"message\": \"上传过程中发生错误\"}");
        }
    }


    @PostMapping("/user/status")
    public UserStatusResponse getUserStatus(@RequestBody UserStatusRequest request) {
        // 这里可以加入实际的业务逻辑，比如从数据库查询用户状态
        // 当前仅返回示例数据
        return new UserStatusResponse("bala", 100,
                new CartItem[]{
                        new CartItem("千恋万花", 100, "1234567890"),
                        new CartItem("怪物猎人", 100, "1234567891"),
                        new CartItem("战地一", 100, "1234567892"),
                        new CartItem("战地二", 100, "1234567893"),
                        new CartItem("战地三", 100, "1234567894"),
                        new CartItem("战地四", 100, "1234567895"),
                        new CartItem("战地五", 100, "1234567896"),
                        new CartItem("战地六", 100, "1234567897"),
                        new CartItem("战地七", 100, "1234567898"),
                        new CartItem("战地八", 100, "1234567899"),
                });
    }

    @PostMapping("/developer/status")
    public ResponseEntity<?> getDeveloperStatus(@RequestBody UserStatusRequest request) {
        // 这里可以加入实际的业务逻辑，比如从数据库查询用户状态
        // 当前仅返回示例数据
        return ResponseEntity.ok().body("{\"developername\": \"bala\","+ "\"balance\": 100}");
    }

    @PostMapping("/user/buy")
    public BuyResponse buySoftware(@RequestBody BuyRequest request) {
        // 这里可以加入实际的业务逻辑，比如更新用户的余额和购物车
        // 当前仅返回示例数据
        return new BuyResponse("success", 10);
    }

    // 请求体对象
    static class UserStatusRequest {
        private String uid;

        // Getter and Setter
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    // 响应体对象
    static class UserStatusResponse {
        private String username;
        private int balance;
        private CartItem[] cart;

        public UserStatusResponse(String username, int balance, CartItem[] cart) {
            this.username = username;
            this.balance = balance;
            this.cart = cart;
        }

        // Getters
        public String getUsername() {
            return username;
        }

        public int getBalance() {
            return balance;
        }

        public CartItem[] getCart() {
            return cart;
        }
    }

    // 购物车条目对象
    static class CartItem {
        private String name;
        private int price;
        private String sid;

        public CartItem(String name, int price, String sid) {
            this.name = name;
            this.price = price;
            this.sid = sid;
        }

        // Getters
        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        public String getSid() {
            return sid;
        }
    }

    // 购买请求体对象
    static class BuyRequest {
        private String uid;
        private String sid;

        // Getters and Setters
        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }

    // 购买响应体对象
    static class BuyResponse {
        private String state;
        private int balance;

        public BuyResponse(String state, int balance) {
            this.state = state;
            this.balance = balance;
        }

        // Getters
        public String getState() {
            return state;
        }

        public int getBalance() {
            return balance;
        }
    }

    @GetMapping("/software_images/{software_id:.+}")
    public ResponseEntity<byte[]> getSoftwareImage(@PathVariable String software_id) {
        System.out.println("imageName: " + software_id);
        try {
            // 从资源目录中加载图片
            //Resource resource = new ClassPathResource("static/images/" + imageName);
            Resource resource = new ClassPathResource("static/images/sbeam2.jpg");
            Path path = Paths.get(resource.getURI());
            byte[] imageData = Files.readAllBytes(path);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "image/png"); // 根据图片类型修改，例如 image/jpeg

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/software/mods/{sid}")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getSoftwareMods(@PathVariable String sid) {
        // 打印sid
        System.out.println("sid: " + sid);
        List<Map<String, String>> mods = new ArrayList<>(Collections.singletonList(
                Map.of("mid", "1234567890", "name", "mod1")
        ));
        mods.add(Map.of("mid", "1234567891", "name", "mod2"));
        mods.add(Map.of("mid", "1234567892", "name", "mod3"));
        return ResponseEntity.ok(Map.of("mods", mods));
    }


    private static final String FILE_PATH = "D:\\data\\test\\"; // 文件存储路径
    @PostMapping("/user/download")
    public ResponseEntity<Map<String, String>> download(@RequestBody Map<String, String> request) throws IOException {
        String type = request.get("type");
        String id = request.get("id");
        //打印type id
        System.out.println("type: " + type);
        System.out.println("id: " + id);

        if (type == null || id == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required parameters"));
        }

        String fileName;
        File file;

        if ("software".equals(type)) {
            fileName = "software" + ".zip";
            file = new File(FILE_PATH + fileName);
        } else if ("mod".equals(type)) {
            //fileName = "mod-" + id + ".zip";
            fileName = "mod" + ".zip";
            file = new File(FILE_PATH + fileName);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid type"));
        }

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String base64File = Base64.getEncoder().encodeToString(fileContent);

        return ResponseEntity.ok(Map.of("file_name", fileName, "file", base64File));
    }

    @GetMapping("/user/lib/{uid}")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getUserSoftwares(@PathVariable String uid) {
        // 示例数据
        List<Map<String, String>> softwares = Arrays.asList(
                Map.of("sid", "1234567890", "name", "game1"),
                Map.of("sid", "1234567891", "name", "game2")
        );
        //打印uid
        System.out.println("uid: " + uid);

        // 构建响应
        Map<String, List<Map<String, String>>> response = Map.of("softwares", softwares);

        return ResponseEntity.ok(response);
    }
}
