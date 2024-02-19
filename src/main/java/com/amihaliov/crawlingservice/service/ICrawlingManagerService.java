package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;

import java.util.List;

public interface ICrawlingManagerService {

    List<Category> getCategoriesToCrawl();

    void setCategoriesToCrawl(List<Category> categories);
}
