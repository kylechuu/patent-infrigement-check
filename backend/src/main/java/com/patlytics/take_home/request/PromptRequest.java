package com.patlytics.take_home.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromptRequest {
    private String patentId;
    private String companyName;
}
