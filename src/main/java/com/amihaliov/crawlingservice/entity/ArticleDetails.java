package com.amihaliov.crawlingservice.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleDetails {

    String articleId;

    String user;

    Integer viewsCount;

    List<String> imgUrls;

    Integer userArticles;

    List<Characteristics> characteristics;
}
