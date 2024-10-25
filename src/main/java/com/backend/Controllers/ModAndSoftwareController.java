package com.backend.Controllers;

import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class ModAndSoftwareController {
    @Autowired
    TrainingDataService trainingDataService;

    @PostMapping("/search")
    public ResponseEntity<Map<String, String>> search(@RequestBody Map<String, String> requestBody) throws SQLException {
        Map<String, String> map = new HashMap<>();
        String keyword = requestBody.get("keyword");

        return null;
    }
}
