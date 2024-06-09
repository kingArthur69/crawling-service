package com.amihaliov.crawlingservice.entity;

import lombok.Data;

import java.util.List;

@Data
public class ParsingResult {

    List<Category> categories;

    List<Article> articles;

    String nextPageUrl;
}
