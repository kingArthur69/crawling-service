package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;

import java.util.List;
import java.util.Set;

public interface ICrawlingManagerService {

    List<Category> getCategoriesToCrawl();

    List<Category> enableCategoriesForCrawling(Set<String> categoryUrls);
}
