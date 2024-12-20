package com.patlytics.take_home.service;

import com.patlytics.take_home.model.ReportEntity;
import com.patlytics.take_home.repository.report.ReportRepository;
import com.patlytics.take_home.response.PromptResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StoreService {
    @Autowired
    ReportRepository reportRepository;

    public ResponseEntity<?> getReport() {
        List<ReportEntity> reports =  reportRepository.findAll();
        return ResponseEntity.status(HttpStatus.CREATED).body(reports);
    }

    public ResponseEntity<?> saveReport(ReportEntity promptReport) {
        try {
            reportRepository.save(promptReport);
            System.out.println("Report saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving report: " + e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<?> deleteReport(String id) {
        reportRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
