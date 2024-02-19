package com.amihaliov.crawlingservice.controller;

import com.amihaliov.crawlingservice.service.ICrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/crawl")
@RequiredArgsConstructor
public class CrawlingController {

    private final ICrawlingService crawlingService;

    @PostMapping("/update")
    ResponseEntity<Object> updateCrawl() {
        try {
//            crawlingService.updateCrawl();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error Update Crawling", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/full")
    ResponseEntity<Object> fullCrawl() {
        try {
//            crawlingService.fullCrawl();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error Update Crawling", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
