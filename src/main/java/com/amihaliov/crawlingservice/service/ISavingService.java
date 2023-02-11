package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Crawl;

import java.util.List;

public interface ISavingService {
    void save(List<Article> articles);

    void save(Crawl crawl);
}
