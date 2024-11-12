package com.patlytics.take_home.service;

import com.google.gson.Gson;
import com.patlytics.take_home.model.CompanyEntity;
import com.patlytics.take_home.model.PatentEntity;
import com.patlytics.take_home.model.dto.*;
import com.patlytics.take_home.repository.company.CompanyRepository;
import com.patlytics.take_home.repository.patent.PatentRepository;
import com.patlytics.take_home.request.GPTRequest;
import com.patlytics.take_home.request.PromptRequest;
import com.patlytics.take_home.response.GPTResponse;
import com.patlytics.take_home.response.OpenAiResponse;
import com.patlytics.take_home.response.PromptResponse;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Value("${openai.model}")
    private String model;

    private final Gson gson = new Gson();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<?> generateReport(PromptRequest request) throws Exception{
        PatentEntity patent = patentRepository.findByPublicationNumber(request.getPatentId()).orElse(null);
        CompanyEntity company = companyRepository.findByName(request.getCompanyName()).orElse(null);
        List<Claim> claims = new ArrayList<>();

        if (patent != null && patent.getClaims() != null) {
            claims = objectMapper.readValue(patent.getClaims(), new TypeReference<List<Claim>>() {});
        }

        int start = 0;
        int end = claims.size();
        int maxSize = 9000;
        List<RelevantFactor> candidates = new ArrayList<>();
        for(Product product: company.getProducts()) {
            RelevantFactor relevantFactor = new RelevantFactor();
            relevantFactor.setProduct(product);
            relevantFactor.setScore(0);
            candidates.add(relevantFactor);
        }

        while (start < end) {
            StringBuilder claimSubset = new StringBuilder();

            while(start < end && claimSubset.toString().length() + claims.get(start).getText().length() < maxSize) {
                claimSubset.append(claims.get(start).getText());
                start += 1;
            }

            List<Product> products = company.getProducts();
            for(int i = 0; i < products.size(); i ++){
                String prompt = this.generatePromptForSubsetClaims(products.get(i), claimSubset.toString());
                String result = this.getCompletion(prompt).block();
                log.info(result);

                // score | index1, index2
                String[] parts = result.split("\\|");
                if(parts.length <= 1) continue;
                log.info("score: {}, index: {}", parts[0], parts[1]);


                int score = candidates.get(i).getScore();
                List<Integer> index = candidates.get(i).getIndex();

                // update score and patent index list
                candidates.get(i).setScore(score + Integer.parseInt(parts[0]));
                candidates.get(i).setIndex(
                        Stream.concat(
                                index.stream(), Arrays.stream(parts[1].split(",")).map(Integer::parseInt))
                        .collect(Collectors.toList())
                );
            }

            start += 1;
        }

        PriorityQueue<RelevantFactor> top2Candidates = new PriorityQueue<>();
        for(RelevantFactor candidate: candidates) {
            addToQueue(top2Candidates, candidate, 2);
        }
        OpenAiResponse openAiResponse = new OpenAiResponse();
        List<ReportDetail> top2InfringingProducts = new ArrayList<>();

        while(!top2Candidates.isEmpty()) {
            RelevantFactor candidate = top2Candidates.poll();
            String top2Prompts = this.generatePromptForTop2Candidates(candidate, claims);
            String[] top2Details = this.getCompletion(top2Prompts).block().split("\\|");
            ReportDetail reportDetail = new ReportDetail();

            reportDetail.setProductName(candidate.getProduct().getName());
            if (candidate.getScore() >= 2)
                reportDetail.setInfringementLikelihood("High");
            else if (candidate.getScore() == 1) {
                reportDetail.setInfringementLikelihood("Moderate");
            } else {
                reportDetail.setInfringementLikelihood("Low");
            }
            reportDetail.setRelevantClaims(candidate.getIndex().stream().map(String::valueOf).collect(Collectors.toList()));

            if(top2Details != null) {
                reportDetail.setExplanation(top2Details[0]);
                if(top2Details.length >= 2) {
                    reportDetail.setSpecificFeatures(Arrays.stream(top2Details[1].split(",")).collect(Collectors.toList()));
                }

            }


            top2InfringingProducts.add(reportDetail);
        }

        openAiResponse.setTopInfringingProducts(top2InfringingProducts);
        String reportPrompt = this.generatePromptForReport(top2InfringingProducts);
        String report = this.getCompletion(reportPrompt).block();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        PromptResponse response = new PromptResponse(
                top2InfringingProducts,
                report,
                LocalDateTime.now().format(formatter),
                request.getCompanyName(),
                request.getPatentId(),
                UUID.randomUUID().toString()

        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public String generatePromptForSubsetClaims(Product product, String claimSubset) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Given a subset of list with it's index and patent claims in Json format string and a company product Json string with it's name and description,Rate the likelihood of patent infringement by the product based on these patents from 0 to 2 (response with number only, 0 stand for irrelevant, 2 stand for completely overlapping) and the index for which patent claim may overlap (a string with index separate with comma). Return a string in the format with: overlap score like 2 | index separate with comma like 1,7,42 e.g. 2|2,5,23 You can also return \"|\" if the overlapping score is 0. Do not return extra string or other character or special character except for the format response");
        prompt.append("Here is subset of claims: " + claimSubset + " with the company information" + product.toString());

        log.info("generatePromptForSubsetClaims -> prompt: {}", prompt);
        return prompt.toString();
    }

    public String generatePromptForTop2Candidates(RelevantFactor candidate, List<Claim> claims) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Given the related patent claims, please return a response string concatenated with \"|\" with 2 prompt's answer. 1st: Return an explanation limit in 200 tokens with the overlapping of claims and product. 2nd: Return a list of specific features separated with comma with limit within 50 tokens in total from the product that potentially infringe the patent e.g. The app's implementation of digital advertisement display and product data handling closely matches the patent's specifications|Mobile app integration,Shopping list synchronization");
        StringBuilder claimsDetail = new StringBuilder();
        for (Integer index: candidate.getIndex()){
            claimsDetail.append(claims.get(index).getText());
        }

        prompt.append("Here is the product detail: " + gson.toJson(candidate.getProduct()) + " the claims detail: " + claimsDetail.toString());

        log.info("generatePromptForTop2Candidates -> prompt: {}", prompt);

        return prompt.toString();
    }

    public String generatePromptForReport(List<ReportDetail> top2InfringingProducts) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Given 2 patent infringing product information, return a risk report limit within 300 tokens to explain how these product may infringe the patent.");
        prompt.append("Here are the 2 patent infringing product information: " + gson.toJson(top2InfringingProducts));

        return prompt.toString();
    }

    public Mono<String> getCompletion(String prompt) {

        GPTRequest gptRequest = new GPTRequest("gpt-3.5-turbo", prompt);

        return webClient.post()
                .bodyValue(gptRequest)
                .retrieve()
                .bodyToMono(GPTResponse.class)
                .map(gptResponse -> {
                    if (gptResponse != null && !gptResponse.getChoices().isEmpty()) {
                        log.info(gson.toJson(gptResponse));
                        return gptResponse.getChoices().get(0).getMessage().getContent().trim();
                    } else {
                        return "Error: No response from OpenAI.";
                    }
                });
    }

//    private String buildRequestBody(String prompt) {
////        Map<String, Object> requestBody = new HashMap<>();
////        requestBody.put("model", model);
////        requestBody.put("prompt", prompt);
////        requestBody.put("max_tokens", tokens);
////        requestBody.put("temperature", temperature);
//
//        GPTRequest gptRequest = new GPTRequest("gpt-3.5-turbo", prompt);
//
//        return requestBody;
//    }

    private static void addToQueue(PriorityQueue<RelevantFactor> queue, RelevantFactor relevantFactor, int maxSize) {
        if (queue.size() >= maxSize) {
            queue.poll();
        }
        queue.add(relevantFactor);
    }
}
