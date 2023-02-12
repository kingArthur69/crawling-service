package com.amihaliov.crawlingservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("crawls")
@Data
@NoArgsConstructor
public class Crawl {

    @Id
    String id;

    LocalDateTime startTimeStamp;

    LocalDateTime endTimeStamp;

    int pagesCrawled;

    String url;

    long articlesCrawled;

    CrawlStatus status = CrawlStatus.IN_PROGRESS;
}
