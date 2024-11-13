package com.backend.Controllers;

import com.backend.Utils.DBConnectClass;
import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/software")
public class SoftwareController {
    @Autowired
    TrainingDataService trainingDataService;

    @GetMapping("/details")
    public ResponseEntity< Map<String, Object>> details(@RequestParam("id") int softwareId) throws SQLException {
        Map<String, Object> map = DBConnectClass.detailsSoftware(softwareId);
        return ResponseEntity.ok(map);
    }
}
