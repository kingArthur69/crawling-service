package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.entity.CrawlStatus;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlExecutorService implements ICrawlExecutorService {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);
    private static final long PAGE_CRAWL_DELAY_MIN = 7L;
    private static final long PAGE_CRAWL_DELAY_MAX = 15L;

    private final ICrawlingService crawlingService;
    private final IParsingService parsingService;
    private final ISavingService savingService;


    public void submitForUpdate(String url) {
        EXECUTOR_SERVICE.execute(() -> processWithPagination(url, 5));
    }

    public void submitForFull(String url) {
        EXECUTOR_SERVICE.execute(() -> processWithPagination(url, 35));
    }

    public void submit(String url) {
        EXECUTOR_SERVICE.execute(() -> process(url));
    }

    private void processWithPagination(String url, int maxPages) {
        Crawl crawl = new Crawl();
        crawl.setUrl(url);
        crawl.setStartTimeStamp(LocalDateTime.now());
        savingService.save(crawl);

        try {
            int count = 0;

            while (count < maxPages) {
                ParsingResult result = process(url);

                count++;

                int articleSize = result.getArticles().size();
                log.info("Crawled url " + url + ". Got " + articleSize + " articles");

                crawl.setPagesCrawled(count);
                crawl.setArticlesCrawled(crawl.getArticlesCrawled() + articleSize);

                if (StringUtils.isNotBlank(result.getNextPageUrl())) {
                    url = result.getNextPageUrl();
                } else break;
            }

            crawl.setStatus(CrawlStatus.FINISHED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            crawl.setStatus(CrawlStatus.ERROR);
        }

        crawl.setEndTimeStamp(LocalDateTime.now());
        savingService.save(crawl);
    }

    private ParsingResult process(String url) {
        try {
            Document document = crawlingService.crawl(url);

            ParsingResult result = parsingService.parse(document);

            savingService.save(result);

            TimeUnit.SECONDS.sleep(RandomUtils.nextLong(PAGE_CRAWL_DELAY_MIN, PAGE_CRAWL_DELAY_MAX));

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Error processing url " + url, e);
        }
    }
}

