package com.amihaliov.crawlingservice.controller;

import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.service.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(SiteController.class)
class SiteControllerTest {

    @MockBean
    private IArticleService articleService;

    @MockBean
    private ICrawlExecutorService crawlExecutorService;

    @MockBean
    private IReadingService readingService;

    @MockBean
    private ICrawlingManagerService crawlingManagerService;

    @MockBean
    private ICategoryService categoryService;

    @Configuration
    static class TestConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void viewHomePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    @Disabled
    void viewCrawlingPage() throws Exception {
        List<Crawl> crawls = List.of(new Crawl());
        when(readingService.findCrawls()).thenReturn(crawls);

        mockMvc.perform(MockMvcRequestBuilders.get("/crawling/update"))
                .andDo(print());
//                .andExpect(MockMvcResultMatchers.view().name("crawling"));
//                .andExpect(MockMvcResultMatchers.model().attribute("crawls", crawls));
    }

    @Test
    void viewCrawlingCategoriesPage() {
    }

    @Test
    void triggerUpdateCrawl() {
    }

    @Test
    void triggerFullCrawl() {
    }

    @Test
    void listCrawls() {
    }

    @Test
    void viewArticlesPage() {
    }

    @Test
    void viewNewArticlesPage() {
    }

    @Test
    void updateCategories() {
    }

    @Test
    void triggerMainPageCrawl() {
    }
}