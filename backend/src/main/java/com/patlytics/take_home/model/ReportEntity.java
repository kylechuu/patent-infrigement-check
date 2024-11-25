package com.patlytics.take_home.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patlytics.take_home.model.dto.ReportDetail;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document(collection = "report")
public class ReportEntity {
    @Id
    private String id; // Assuming MongoDB uses String for ID

    @Field("analysis_id")
    @JsonProperty("analysis_id")
    private String analysisId;

    @Field("patent_id")
    @JsonProperty("patent_id")
    private String patentId;

    @Field("company_name")
    @JsonProperty("company_name")
    private String companyName;

    @Field("analysis_date")
    @JsonProperty("analysis_date")
    private String analysisDate;

    @Field("top_infringing_products")
    @JsonProperty("top_infringing_products")
    private List<ReportDetail> topInfringingProducts;

    @Field("overall_risk_assessment")
    @JsonProperty("overall_risk_assessment")
    private String overallRiskAssessment;
}
