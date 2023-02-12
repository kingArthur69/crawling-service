package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.repository.ArticleRepository;
import com.amihaliov.crawlingservice.repository.CrawlRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
class CrawlingServiceImplTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ICrawlingService crawlingService;

    @Test
    void crawl() {
        crawlingService.updateCrawl();
    }

    @Test
    void fullCrawl() {
        crawlingService.fullCrawl();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ICrawlingService crawlingService(ISavingService savingService, IParsingService parsingService) {
            return new CrawlingServiceImpl(savingService, parsingService);
        }

        @Bean
        ISavingService savingService(ArticleRepository articleRepository, CrawlRepository crawlRepository) {
            return new SavingServiceImpl(articleRepository, crawlRepository);
        }

        @Bean
        IParsingService parsingService() {
            return new ParsingServiceImpl();
        }
    }

}