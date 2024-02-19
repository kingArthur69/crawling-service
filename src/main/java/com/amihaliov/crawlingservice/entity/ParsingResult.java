package com.amihaliov.crawlingservice.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Data
public class ParsingResult {

    List<Category> categories;

    List<Article> articles;

    String nextPageUrl;
}
