package com.patlytics.take_home.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patlytics.take_home.model.dto.Product;
import com.patlytics.take_home.model.dto.ReportDetail;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
public class PromptResponse extends OpenAiResponse{
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

    public PromptResponse(
            List<ReportDetail> topInfringingProducts,
                          String overallRiskAssessment,
                          String analysisDate,
                          String companyName,
                          String patentId,
                          String analysisId
            )
    {
        super(topInfringingProducts, overallRiskAssessment);
        this.analysisDate = analysisDate;
        this.companyName = companyName;
        this.patentId = patentId;
        this.analysisId = analysisId;
    }
}
