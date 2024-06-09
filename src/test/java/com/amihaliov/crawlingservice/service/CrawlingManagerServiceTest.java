package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrawlingManagerServiceTest {

    @Mock
    private ICategoryService categoryService;

    @Mock
    private ISavingService savingService;

    @InjectMocks
    private CrawlingManagerService crawlingManagerService;

    @Test
    void getCategoriesToCrawl() {
        Category enabledCategory = new Category();
        enabledCategory.setEnabled(true);

        when(categoryService.findAllCategories()).thenReturn(List.of(enabledCategory, new Category()));

        assertEquals(List.of(enabledCategory), crawlingManagerService.getCategoriesToCrawl());
    }

    @Test
    void enableCategoriesForCrawling() {
        Category mainCategory = createCategory("containedMainUrl");
        mainCategory.setSubcategories(List.of(createCategory("notContainedSubUrl")));

        Category notContainedMainCategory = createCategory("notContainedMainUrl");
        notContainedMainCategory.setSubcategories(List.of(createCategory("containedSubUrl")));

        when(categoryService.findMainCategories()).thenReturn(List.of(mainCategory, notContainedMainCategory));

        List<Category> categories = crawlingManagerService.enableCategoriesForCrawling(Set.of("containedMainUrl", "containedSubUrl"));

        verify(savingService).saveCategories(any());

        assertEquals(2, categories.size());
        assertMainAndSubCategories(categories.get(0), "containedMainUrl", true, "notContainedSubUrl", false);
        assertMainAndSubCategories(categories.get(1), "notContainedMainUrl", false, "containedSubUrl", true);
    }

    private static void assertMainAndSubCategories(Category mainCategory, String mainUrl, boolean mainEnabled, String subUrl, boolean subEnabled) {
        assertEquals(mainUrl, mainCategory.getUrl());
        assertEquals(mainEnabled, mainCategory.isEnabled());

        Category subCategory = mainCategory.getSubcategories().get(0);
        assertEquals(subUrl, subCategory.getUrl());
        assertEquals(subEnabled, subCategory.isEnabled());
    }

    private static Category createCategory(String url) {
        Category mainCategory = new Category();
        mainCategory.setUrl(url);
        return mainCategory;
    }
}