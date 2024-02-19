package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;

import java.util.List;

public interface ICategoryService {

    List<Category> findAllCategories();

    List<Category> findMainCategories();

    List<Category> findByNameAndMarketplace(String name, String marketplace);
}
