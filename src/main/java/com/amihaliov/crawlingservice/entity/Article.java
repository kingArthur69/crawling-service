package com.amihaliov.crawlingservice.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document("articles")
@Data
@Builder
public class Article {

    @Id
    String id;

    String imgUrl;

    LocalDateTime lastUpdateTime;

    LocalDateTime updateTimeStamp;

    LocalDateTime createTimeStamp;

    String url;

    String title;

    String description;

    Price currentPrice;

    Map<String, Price> priceHistory;

    String phone;
}
