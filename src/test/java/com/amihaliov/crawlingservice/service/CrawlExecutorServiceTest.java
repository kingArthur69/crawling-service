package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"pageCrawlDelaySecondsMin=0", "pageCrawlDelaySecondsMax=0"})
class CrawlExecutorServiceTest {

    public static final String TEST_URL = "testUrl";
    @Mock
    private ICrawlingService crawlingService;

    @Mock
    private IParsingService parsingService;

    @Mock
    private ISavingService savingService;

    @InjectMocks
    private CrawlExecutorService crawlExecutorService;

    @Captor
    ArgumentCaptor<Crawl> captor;

    @Test
    void submitForUpdate() {
        ParsingResult result = new ParsingResult();
        result.setArticles(List.of(Article.builder().build()));
        result.setNextPageUrl(TEST_URL);
        when(parsingService.parse(any())).thenReturn(result);

        crawlExecutorService.submitForUpdate(TEST_URL);

        verify(crawlingService, timeout(100L).times(5)).crawl(TEST_URL);
        verify(parsingService, times(5)).parse(any());
        verify(savingService, times(5)).save(result);
        verify(savingService, times(7)).save(captor.capture());

        Crawl crawl = captor.getValue();
        assertEquals(5, crawl.getPagesCrawled());
    }

    @Test
    void submit() {
        ParsingResult result = new ParsingResult();
        when(parsingService.parse(any())).thenReturn(result);

        crawlExecutorService.submit(TEST_URL);

        verify(crawlingService, timeout(100L)).crawl(TEST_URL);
        verify(parsingService).parse(any());
        verify(savingService).save(result);
    }
}