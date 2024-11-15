package com.patlytics.take_home.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class Product {
    @Field("name")
    private String name;
    @Field("description")
    private String description;
}
