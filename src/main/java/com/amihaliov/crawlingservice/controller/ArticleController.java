package com.amihaliov.crawlingservice.controller;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.service.IArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleService articleService;

    @GetMapping("/")
    ResponseEntity<List<Article>> getArticles(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "count", defaultValue = "50") Integer count,
            @RequestParam(name = "order", defaultValue = "lastUpdateTime") String property,
            @RequestParam(name = "direction", defaultValue = "desc") String direction
    ) {
        try {
            Sort sortBy = Sort.by(Sort.Direction.fromString(direction), property);
            Pageable pageable = PageRequest.of(page, count, sortBy);
            List<Article> articles = articleService.getArticlesPage(pageable).getContent();
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            log.error("Error while getting Articles", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/new")
    ResponseEntity<List<Article>> getNewArticles() {
        try {
            List<Article> articles = articleService.getNewArticles();
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            log.error("Error while getting New Articles", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
