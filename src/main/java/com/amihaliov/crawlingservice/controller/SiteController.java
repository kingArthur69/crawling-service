package com.amihaliov.crawlingservice.controller;


import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.service.IArticleService;
import com.amihaliov.crawlingservice.service.ICrawlingService;
import com.amihaliov.crawlingservice.service.IReadingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SiteController {

    private final IArticleService articleService;

    private final ICrawlingService crawlingService;

    private final IReadingService readingService;

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

    @GetMapping("/crawling/update")
    String triggerUpdateCrawl() {
        crawlingService.updateCrawl();
        return "redirect:/crawling";
    }

    @GetMapping("/crawling/full")
    String triggerFullCrawl() {
        crawlingService.fullCrawl();
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
            Model model
    ) {
        try {
            Sort sortBy = Sort.by(Sort.Direction.fromString(direction), property);
            Pageable pageable = PageRequest.of(page, count, sortBy);
            Page<Article> articlesPage = articleService.getArticlesPage(pageable);
            model.addAttribute("articles", articlesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("pages", articlesPage.getTotalPages());
            model.addAttribute("elements", articlesPage.getTotalElements());
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

}
