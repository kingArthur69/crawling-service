package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleServiceImpl;

    @Captor
    private ArgumentCaptor<TextCriteria> criteria;

    @Captor
    private ArgumentCaptor<LocalDateTime> localDateTime;

    private static Stream<Arguments> textSupplier() {
        return Stream.of(
                Arguments.of("\"quoted test\"", "\"quoted test\""),
                Arguments.of("test", "test")
        );
    }

    private static Stream<String> incorrectTextSupplier() {
        return Stream.of(null, "", "ab", "abcdefghklmnoprstabcdefghklmnoprst");
    }

    @ParameterizedTest
    @MethodSource("textSupplier")
    void getArticlesPageByTextTest(String input, String expected) {
        articleServiceImpl.getArticlesPageByText(input, PageRequest.of(1, 1));

        verify(articleRepository).findAllBy(criteria.capture(), any());

        TextCriteria expectedCriteria = getTextCriteria(expected);

        assertEquals(expectedCriteria, criteria.getValue());
    }

    @Test
    void getNewArticlesTest() {
        articleServiceImpl.getNewArticles();

        LocalDateTime expectedTime = LocalDateTime.now().minusHours(2L).withNano(0);

        verify(articleRepository).findByUpdateTimeStampNullAndLastUpdateTimeAfterAndCreateTimeStampAfter(
                localDateTime.capture(), any()
        );
        assertEquals(expectedTime, localDateTime.getValue().withNano(0));
    }

    @Test
    void url_and_text_correct_getArticlePageByUrlsStartingAndTextCriteriaTest() {
        articleServiceImpl.getArticlePageByUrlsStartingAndTextCriteria("transnistria/testUrl", "testText", Pageable.unpaged());

        verify(articleRepository).findAllByUrlStartsWith(eq("testUrl"), eq(getTextCriteria("testText")), any());
    }

    @Test
    void text_correct_getArticlePageByUrlsStartingAndTextCriteriaTest() {
        articleServiceImpl.getArticlePageByUrlsStartingAndTextCriteria(null, "testText", Pageable.unpaged());

        verify(articleRepository).findAllBy(eq(getTextCriteria("testText")), any());
    }

    @ParameterizedTest
    @MethodSource("incorrectTextSupplier")
    void url_correct_getArticlePageByUrlsStartingAndTextCriteriaTest(String text) {
        articleServiceImpl.getArticlePageByUrlsStartingAndTextCriteria("transnistria/testUrl", text, Pageable.unpaged());

        verify(articleRepository).findByUrlStartsWith(eq("testUrl"), any());
    }

    @Test
    void getArticlePageByUrlsStartingAndTextCriteriaTest() {
        articleServiceImpl.getArticlePageByUrlsStartingAndTextCriteria(null, null, Pageable.unpaged());

        verify(articleRepository).findAll(any(Pageable.class));
    }

    private static TextCriteria getTextCriteria(String expected) {
        return TextCriteria
                .forDefaultLanguage()
                .caseSensitive(false)
                .matching(expected);
    }
}