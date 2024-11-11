package com.patlytics.take_home.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patlytics.take_home.model.dto.ReportDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OpenAiResponse {

    @Field("top_infringing_products")
    @JsonProperty("top_infringing_products")
    private List<ReportDetail> topInfringingProducts;

    @Field("overall_risk_assessment")
    @JsonProperty("overall_risk_assessment")
    private String overallRiskAssessment;

}
