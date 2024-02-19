package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.entity.Crawl;

import java.util.List;

public interface IReadingService {
    List<Crawl> findCrawls();
}
