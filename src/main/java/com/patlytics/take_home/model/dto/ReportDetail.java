package com.patlytics.take_home.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
public class ReportDetail {
    @Field("product_name")
    @JsonProperty("product_name")
    private String productName;

    @Field("infringement_likelihood")
    @JsonProperty("infringement_likelihood")
    private String infringementLikelihood;

    @Field("relevant_claims")
    @JsonProperty("relevant_claims")
    private List<String> relevantClaims;

    @Field("explanation")
    @JsonProperty("explanation")
    private String explanation;

    @Field("specific_features")
    @JsonProperty("specific_features")
    private List<String> specificFeatures;

}
