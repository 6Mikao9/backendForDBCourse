package com.backend.service;

import com.backend.vo.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

@Slf4j
@Service
public class TrainingDataService {
    public DataID uploadData(String type, String name, MultipartFile content) {
        return new DataID("12345");
    }
}
