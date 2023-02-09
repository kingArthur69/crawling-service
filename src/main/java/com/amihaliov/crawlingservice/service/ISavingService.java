package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;

import java.util.List;

public interface ISavingService {
    void save(List<Article> articles);
}
