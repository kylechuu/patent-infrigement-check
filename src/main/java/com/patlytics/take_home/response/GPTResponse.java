package com.patlytics.take_home.response;

import com.patlytics.take_home.model.dto.Choice;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GPTResponse {
    private List<Choice> choices;
}
