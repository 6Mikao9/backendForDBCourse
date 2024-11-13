package com.backend.Controllers;

import com.backend.Utils.DBConnectClass;
import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/software")
public class SoftwareController {
    @Autowired
    TrainingDataService trainingDataService;

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> details(@RequestParam("id") int softwareId) throws SQLException {
        Map<String, Object> map = DBConnectClass.detailsSoftware(softwareId);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/mods/{sid}")
    public ResponseEntity<?> mods(@PathVariable("sid") int softwareId) throws SQLException {
        if (!DBConnectClass.isSoftwareById(softwareId)) {
            return ResponseEntity.notFound().build();
        }

        ArrayList<Map> arrayList = DBConnectClass.modsOfSoftware(softwareId);
        Map<String, ArrayList> map = new HashMap<>();
        map.put("mods", arrayList);
        return ResponseEntity.ok(map);
    }
}
