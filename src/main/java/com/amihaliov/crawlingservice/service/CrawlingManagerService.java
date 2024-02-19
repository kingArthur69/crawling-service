package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrawlingManagerService implements ICrawlingManagerService {

    private final ICategoryService categoryService;

    private List<Category> categories;

    @PostConstruct
    public void setUp() {
        List<Category> allCategories = categoryService.findAllCategories();
        categories = allCategories.stream()
                .filter(c -> StringUtils.equals(c.getName(), "Легковые автомобили"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getCategoriesToCrawl() {
        return categories;
    }

    @Override
    public void setCategoriesToCrawl(List<Category> categories) {
        this.categories = categories;
    }
}
