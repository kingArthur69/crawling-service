package com.amihaliov.crawlingservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("categories")
@Data
public class Category {

    @Id
    String url;

    String marketplace;

    String name;

    List<Category> subcategories;
}
