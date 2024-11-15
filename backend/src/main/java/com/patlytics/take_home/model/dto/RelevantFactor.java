package com.patlytics.take_home.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RelevantFactor implements Comparable<RelevantFactor> {
    private int score;
    private List<Integer> index = new ArrayList<>();
    private Product product;

    @Override
    public int compareTo(RelevantFactor other) {
        return Integer.compare(other.score, this.score);  // Compare based on age
    }
}
