package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategories() {
        List<Category> mainCategories = categoryRepository.findAll();
        List<Category> subcategories = mainCategories.stream()
                .map(Category::getSubcategories)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        mainCategories.addAll(subcategories);
        return new ArrayList<>(mainCategories);
    }

    @Override
    public List<Category> findMainCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findByNameAndMarketplace(String name, String marketplace) {
        return categoryRepository.findAllByNameAndMarketplace(name, marketplace);
    }
}
