package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.entity.ParsingResult;

import java.util.List;

public interface ISavingService {
    void save(List<Article> articles);

    void save(Crawl crawl);

    void save(ParsingResult result);

    void saveCategories(List<Category> categories);
}
