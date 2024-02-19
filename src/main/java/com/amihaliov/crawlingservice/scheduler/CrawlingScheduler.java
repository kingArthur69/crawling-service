package com.amihaliov.crawlingservice.scheduler;

import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.service.CrawlExecutorService;
import com.amihaliov.crawlingservice.service.ICrawlExecutorService;
import com.amihaliov.crawlingservice.service.ICrawlingManagerService;
import com.amihaliov.crawlingservice.service.IReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private static final String BASE_URL = "https://makler.md/ru/categories";

    private final ICrawlingManagerService crawlingManagerService;
    private final ICrawlExecutorService crawlExecutorService;

    @Scheduled(cron = "${cron.categoryCrawlExpression}")
    public void scheduleCategoryCrawl() {
        crawlExecutorService.submit(BASE_URL);
    }

    @Scheduled(cron = "${cron.updateCrawlExpression}")
    public void scheduleListingCrawl() {
        List<Category> categories = getAllCategoriesForCrawl();

        for (Category category : categories) {
            crawlExecutorService.submitForUpdate(category.getUrl());
        }
    }

    @Scheduled(cron = "${cron.fullCrawlExpression}")
    public void scheduleFullListingCrawl() {
        List<Category> categories = getAllCategoriesForCrawl();

        for (Category category : categories) {
            crawlExecutorService.submitForFull(category.getUrl());
        }
    }

    private List<Category> getAllCategoriesForCrawl() {
        return crawlingManagerService.getCategoriesToCrawl();
    }
}
