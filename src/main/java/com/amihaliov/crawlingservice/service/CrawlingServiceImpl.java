package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.utils.ParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlingServiceImpl implements ICrawlingService {

    public static final String BASE_URL = "https://makler.md/ru/transnistria/transport/cars";
    public static final long PAGE_CRAWL_DELAY_MIN = 7L;
    public static final long PAGE_CRAWL_DELAY_MAX = 15L;
    public static final String NEXT_PAGE_SELECTOR = "li a.paginator_pageLink.paginator_nextPage";

    private final ISavingService savingService;

    private final IParsingService parsingService;

    @Override
    public void updateCrawl() {
        crawlPaginated(3);
    }

    @Override
    public void fullCrawl() {
        crawlPaginated(35);
    }

    private void crawlPaginated(int maxPages) {
        try {
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

                log.info("Crawled url " + url + ". Got " + articles.size() + " articles");
                TimeUnit.SECONDS.sleep(RandomUtils.nextLong(PAGE_CRAWL_DELAY_MIN, PAGE_CRAWL_DELAY_MAX));
                count++;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
