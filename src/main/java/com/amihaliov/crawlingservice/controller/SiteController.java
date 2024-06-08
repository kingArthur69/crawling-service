package com.amihaliov.crawlingservice.controller;


import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SiteController {

    private final IArticleService articleService;

    private final ICrawlExecutorService crawlExecutorService;

    private final IReadingService readingService;

    private final ICrawlingManagerService crawlingManagerService;

    private final ICategoryService categoryService;

    @GetMapping("/")
    String viewHomePage() {
        return "index";
    }

    @GetMapping("/crawling")
    String viewCrawlingPage(Model model) {
        List<Crawl> crawls = readingService.findCrawls();
        model.addAttribute("crawls", crawls);
        return "crawling";
    }

    @GetMapping("/crawling/categories")
    String viewCrawlingCategoriesPage(Model model) {
        List<Category> mainCategories = categoryService.findMainCategories();
        model.addAttribute("mainCategories", mainCategories);
        return "categories";
    }

    @GetMapping("/crawling/update")
    String triggerUpdateCrawl() {
        for (Category category : crawlingManagerService.getCategoriesToCrawl()) {
            crawlExecutorService.submitForUpdate(category.getUrl());
        }
        return "redirect:/crawling";
    }

    @GetMapping("/crawling/full")
    String triggerFullCrawl() {
        for (Category category : crawlingManagerService.getCategoriesToCrawl()) {
            crawlExecutorService.submitForFull(category.getUrl());
        }
        return "redirect:/crawling";
    }

    @GetMapping("/crawling/list")
    String listCrawls(Model model) {
        List<Crawl> crawls = readingService.findCrawls();
        model.addAttribute("crawls", crawls);
        return "crawling";
    }

    @GetMapping("/articles")
    String viewArticlesPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "count", defaultValue = "50") Integer count,
            @RequestParam(name = "order", defaultValue = "lastUpdateTime") String property,
            @RequestParam(name = "direction", defaultValue = "desc") String direction,
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "categoryUrl", required = false) String categoryUrl,
            Model model
    ) {
        try {
            Sort sortBy = Sort.by(Sort.Direction.fromString(direction), property);
            Pageable pageable = PageRequest.of(page, count, sortBy);
            String trimmedText = StringUtils.trim(text);

            Page<Article> articlesPage = articleService.getArticlePageByUrlsStartingAndTextCriteria(categoryUrl, trimmedText, pageable);

            model.addAttribute("articles", articlesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("pages", articlesPage.getTotalPages());
            model.addAttribute("elements", articlesPage.getTotalElements());
            model.addAttribute("text", text);
            model.addAttribute("categoryUrl", categoryUrl);
            model.addAttribute("categories", categoryService.findAllCategories());
            return "articles";
        } catch (Exception e) {
            log.error("Error while getting Articles", e);
            return "error";
        }
    }

    @GetMapping("/articles/new")
    String viewNewArticlesPage(Model model) {
        try {
            List<Article> articles = articleService.getNewArticles();
            model.addAttribute("articles", articles);
            return "newArticles";
        } catch (Exception e) {
            log.error("Error while getting Articles", e);
            return "error";
        }
    }

    @PostMapping(path = "/category", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    String updateCategories(@RequestParam MultiValueMap<String, String> params, Model model) {
        try {
            List<Category> updatedCategories = crawlingManagerService.enableCategoriesForCrawling(params.keySet());

            model.addAttribute("mainCategories", updatedCategories);

            return "categories";
        } catch (Exception e) {
            log.error("Error while updating Categories", e);
            return "error";
        }
    }

}
