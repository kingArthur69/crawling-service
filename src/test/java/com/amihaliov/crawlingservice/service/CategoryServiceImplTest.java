package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAllCategories() {
        Category mainCategory = new Category();
        mainCategory.setName("mainCategory");

        Category subCategory = new Category();
        subCategory.setName("subcategory");

        mainCategory.setSubcategories(List.of(subCategory));

        when(categoryRepository.findAll()).thenReturn(new ArrayList<>(List.of(mainCategory)));

        assertEquals(List.of(mainCategory, subCategory), categoryService.findAllCategories());
    }
}