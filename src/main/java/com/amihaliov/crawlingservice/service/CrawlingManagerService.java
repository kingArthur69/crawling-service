package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrawlingManagerService implements ICrawlingManagerService {

    private final ICategoryService categoryService;

    private final ISavingService savingService;

    @Override
    public List<Category> getCategoriesToCrawl() {
        return categoryService.findAllCategories().stream()
                .filter(Category::isEnabled)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> enableCategoriesForCrawling(Set<String> categoryUrls) {
        List<Category> mainCategories = categoryService.findMainCategories();
        for (Category mainCategory : mainCategories) {
            mainCategory.setEnabled(categoryUrls.contains(mainCategory.getUrl()));

            List<Category> subcategories = mainCategory.getSubcategories();
            if (!CollectionUtils.isEmpty(subcategories)) {
                for (Category subcategory : subcategories) {
                    subcategory.setEnabled(categoryUrls.contains(subcategory.getUrl()));
                }
            }
        }

        savingService.saveCategories(mainCategories);

        return mainCategories;
    }
}
