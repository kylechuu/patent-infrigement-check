package com.patlytics.take_home.controller;

import com.patlytics.take_home.request.PromptRequest;
import com.patlytics.take_home.response.PromptResponse;
import com.patlytics.take_home.service.PromptService;
import com.patlytics.take_home.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ComponentScan
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class PromptController {
    @Autowired
    private PromptService promptService;
    @Autowired
    private StoreService storeService;

    @PostMapping("/generate-report")
    public ResponseEntity<?> generateReport(@RequestBody PromptRequest promptRequest) throws Exception {
        return promptService.generateReport(promptRequest);
    }

    @PutMapping("/save-report")
    public ResponseEntity<?> saveReport(@RequestBody PromptResponse promptReport) throws Exception {
        return storeService.saveReport(promptReport);
    }

    @DeleteMapping("/delete-report")
    public ResponseEntity<?> deleteReport(@PathVariable String id) throws Exception {
        return storeService.deleteReport(id);
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getHistory() throws Exception {
        return storeService.getReport();
    }

}
