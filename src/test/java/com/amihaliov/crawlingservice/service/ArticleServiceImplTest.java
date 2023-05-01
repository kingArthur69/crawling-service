package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.repository.ArticleRepository;
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
import org.springframework.data.mongodb.core.query.TextCriteria;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleServiceImpl;

    @Captor
    ArgumentCaptor<TextCriteria> criteria;

    private static Stream<Arguments> textSupplier() {
        return Stream.of(
                Arguments.of("\"quoted test\"", "\"quoted test\""),
                Arguments.of("test", "test")
        );
    }

    @ParameterizedTest
    @MethodSource("textSupplier")
    void getArticlesPageByTextTest(String input, String expected) {
        articleServiceImpl.getArticlesPageByText(input, PageRequest.of(1, 1));

        verify(articleRepository).findAllBy(criteria.capture(), any());

        TextCriteria expectedCriteria = TextCriteria
                .forDefaultLanguage()
                .caseSensitive(false)
                .matching(expected);

        assertEquals(expectedCriteria, criteria.getValue());
    }
}