package com.patlytics.take_home.service;

import com.google.gson.Gson;
import com.patlytics.take_home.model.CompanyEntity;
import com.patlytics.take_home.model.PatentEntity;
import com.patlytics.take_home.model.dto.Claim;
import com.patlytics.take_home.repository.company.CompanyRepository;
import com.patlytics.take_home.repository.patent.PatentRepository;
import com.patlytics.take_home.request.PromptRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PromptService {
    @Autowired
    private PatentRepository patentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private WebClient webClient;

    @Value("${openai.tokens}")
    private String tokens;
    @Value("${openai.temperature}")
    private String temperature;

    private final Gson gson = new Gson();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<?> generateReport(PromptRequest request) throws Exception{
        PatentEntity patent = patentRepository.findByPublicationNumber(request.getPatentId()).orElse(null);
        CompanyEntity company = companyRepository.findByName(request.getCompanyName()).orElse(null);
        List<Claim> claims = new ArrayList<>();

        if (patent != null && patent.getClaims() != null) {
            claims = objectMapper.readValue(patent.getClaims(), new TypeReference<List<Claim>>() {});
        }
        if (claims != null) {
            for (Claim claim: claims) {
                log.info("claim name: {}, claim desc: {}.", claim.getNum(), claim.getText());
            }
        }

        String prompt = GeneratePrompt(patent, company) {

        }




        return ResponseEntity.status(HttpStatus.CREATED).body(patent.getClaims());
    }

    public String GeneratePrompt(PatentEntity patent, CompanyEntity company) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Given a list of product from a company and a Patent object in Json format, Please list top two product from that company");


        return prompt.toString();
    }

    public Mono<String> getCompletion(String prompt) {
        return webClient.post()
                .uri("/completions")
                .bodyValue(buildRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class);
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", tokens);
        requestBody.put("temperature", temperature);
        return requestBody;
    }
}
