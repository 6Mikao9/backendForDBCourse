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
@RequestMapping("api/message")
public class MessageController {
    @Autowired
    TrainingDataService trainingDataService;

    @GetMapping("/query/{id}")
    public ResponseEntity<?> query(@PathVariable("id") int id) throws SQLException {
        ArrayList<Map<String, Object>> arrayList = DBConnectClass.queryMessage(id);
        Map<String, ArrayList> map = new HashMap<>();
        map.put("message", arrayList);
        return ResponseEntity.ok(map);
    }


    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> send(@RequestParam("sendId") int sendId, @RequestParam("content") String content, @RequestParam("receiveId") int receiveId) throws SQLException {
        Map<String, String> map = new HashMap<>();
        if (content.isEmpty()) {
            map.put("result", "FAIL");
            return ResponseEntity.badRequest().body(map);
        }

        if (DBConnectClass.sendMessage(sendId, receiveId, content)) {
            map.put("result", "SUC");
            return ResponseEntity.ok(map);
        } else {
            map.put("result", "FAIL");
            return ResponseEntity.badRequest().body(map);
        }
    }

}
