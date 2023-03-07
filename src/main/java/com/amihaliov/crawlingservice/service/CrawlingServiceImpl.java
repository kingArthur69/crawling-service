package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.entity.CrawlStatus;
import com.amihaliov.crawlingservice.utils.ParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlingServiceImpl implements ICrawlingService {

    public static final String BASE_URL = "https://makler.md/ru/transnistria/transport/cars";
    public static final long PAGE_CRAWL_DELAY_MIN = 7L;
    public static final long PAGE_CRAWL_DELAY_MAX = 15L;
    public static final String NEXT_PAGE_SELECTOR = "li a.paginator_pageLink.paginator_nextPage";

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final ISavingService savingService;

    private final IParsingService parsingService;

    @Override
    public void updateCrawl() {
        executorService.submit(() -> crawl(5));
    }

    @Override
    public void fullCrawl() {
        executorService.submit(() -> crawl(35));
    }

    private void crawl(int maxPages) {
        Crawl crawl = new Crawl();
        crawl.setStartTimeStamp(LocalDateTime.now());
        savingService.save(crawl);

        crawl = crawlPaginated(maxPages, crawl);

        crawl.setEndTimeStamp(LocalDateTime.now());
        savingService.save(crawl);
    }

    private Crawl crawlPaginated(int maxPages, Crawl crawl) {
        try {
            crawl.setUrl(BASE_URL);

            String url = BASE_URL;

            int count = 0;

            while (count < maxPages) {
                Document document = Jsoup.connect(url).get();
                List<Article> articles = parsingService.parse(document);

                savingService.save(articles);

                String nextPage = ParserUtils.getAttr(document, NEXT_PAGE_SELECTOR, ParserUtils.HREF_ATTR);

                if (StringUtils.isNotBlank(nextPage)) {
                    url = BASE_URL + nextPage;
                } else break;

                int articleSize = articles.size();
                log.info("Crawled url " + url + ". Got " + articleSize + " articles");
                TimeUnit.SECONDS.sleep(RandomUtils.nextLong(PAGE_CRAWL_DELAY_MIN, PAGE_CRAWL_DELAY_MAX));
                count++;

                crawl.setPagesCrawled(count);
                crawl.setArticlesCrawled(crawl.getArticlesCrawled() + articleSize);
            }

            crawl.setStatus(CrawlStatus.FINISHED);
            return crawl;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            crawl.setStatus(CrawlStatus.ERROR);
            return crawl;
        }
    }

}
