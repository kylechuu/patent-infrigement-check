package com.patlytics.take_home.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document(collection = "patents")
public class PatentEntity {
    @Id
    private String id; // Assuming MongoDB uses String for ID

    @JsonProperty("publication_number")
    @Field("publication_number")
    private String publicationNumber;

    @JsonProperty("title")
    @Field("title")
    private String title;

    @JsonProperty("ai_summary")
    @Field("ai_summary")
    private String aiSummary;

    @JsonProperty("raw_source_url")
    @Field("raw_source_url")
    private String rawSourceUrl;

    @JsonProperty("assignee")
    @Field("assignee")
    private String assignee;

    @JsonProperty("inventors")
    @Field("inventors")
    private String inventors; // Assuming inventors will be a list of Inventor objects

    @JsonProperty("priority_date")
    @Field("priority_date")
    private String priorityDate;

    @JsonProperty("application_date")
    @Field("application_date")
    private String applicationDate;

    @JsonProperty("grant_date")
    @Field("grant_date")
    private String grantDate;

    @JsonProperty("abstract") // Renamed to avoid conflict with the keyword
    @Field("abstract")
    private String abstractText;

    @JsonProperty("description")
    @Field("description")
    private String description;

    @JsonProperty("claims")
    @Field("claims")
    private String claims; // Assuming claims will be a list of Claim objects

    // Nested classes for Inventor and Claim
    @Data
    public static class Inventor {
        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;
    }
}

