package com.amihaliov.crawlingservice.repository;

import com.amihaliov.crawlingservice.entity.Crawl;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CrawlRepository extends MongoRepository<Crawl, String> {

}