package com.patlytics.take_home.controller;

import com.patlytics.take_home.request.PromptRequest;
import com.patlytics.take_home.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ComponentScan
@RestController
@RequestMapping("/api/v1")
public class PromptController {
    @Autowired
    private PromptService promptService;

    @GetMapping("/generate-report")
    public ResponseEntity<?> generateReport(@RequestBody PromptRequest promptRequest) throws Exception{
        return promptService.generateReport(promptRequest);
    }

}
