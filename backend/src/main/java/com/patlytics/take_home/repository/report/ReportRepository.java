package com.patlytics.take_home.repository.report;

import com.patlytics.take_home.response.PromptResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<PromptResponse, String> {
//    PromptResponse insert(PromptResponse promptReport);
//    ListM<PromptResponse> findall();
}
