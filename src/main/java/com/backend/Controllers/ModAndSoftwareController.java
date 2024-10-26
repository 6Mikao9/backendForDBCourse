package com.backend.Controllers;

import com.backend.service.TrainingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
