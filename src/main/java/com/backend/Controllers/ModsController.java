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
import java.util.Map;

@RestController
@RequestMapping("api/mods")
public class ModsController {
    @Autowired
    TrainingDataService trainingDataService;

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> details(@RequestParam("id") int modId) throws SQLException {
        Map<String, Object> map = DBConnectClass.detailsMod(modId);
        return ResponseEntity.ok(map);
    }
}
