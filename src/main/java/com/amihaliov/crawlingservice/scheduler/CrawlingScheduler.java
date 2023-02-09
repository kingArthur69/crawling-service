package com.amihaliov.crawlingservice.scheduler;

import com.amihaliov.crawlingservice.service.ICrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final ICrawlingService crawlingService;

    @Scheduled(cron = "${cron.updateCrawlExpression}")
    public void scheduleCrawl() {

        crawlingService.updateCrawl();
    }

    @Scheduled(cron = "${cron.fullCrawlExpression}")
    public void scheduleFullCrawl() {

        crawlingService.fullCrawl();
    }
}
