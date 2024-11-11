package com.patlytics.take_home.model;

import com.patlytics.take_home.model.dto.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document(collection = "companies")
public class CompanyEntity {

    @Id
    private String Id;

    @Field("name")
    private String name;

    @Field("products")
    private List<Product> products;

}
