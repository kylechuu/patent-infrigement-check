package com.patlytics.take_home.request;

import com.patlytics.take_home.model.dto.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GPTRequest {
    private String model;
    private List<Message> messages = new ArrayList<>();

    public GPTRequest(String model, String prompt) {
        this.model = model;
        this.messages.add(new Message("user", prompt));
    }
}
