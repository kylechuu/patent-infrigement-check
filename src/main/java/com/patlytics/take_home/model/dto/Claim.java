package com.patlytics.take_home.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Claim {
    @JsonProperty("num")
    private String num;

    @JsonProperty("text")
    private String text;
}
