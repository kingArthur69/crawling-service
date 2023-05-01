package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements IArticleService {

    private static final int NEW_ARTICLE_TIME_THRESHOLD = 2;

    private final ArticleRepository articleRepository;

    @Override
    public Page<Article> getArticlesPage(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public List<Article> getNewArticles() {
        LocalDateTime newArticleTime = LocalDateTime.now().minusHours(NEW_ARTICLE_TIME_THRESHOLD);
        return articleRepository.findByUpdateTimeStampNullAndLastUpdateTimeAfterAndCreateTimeStampAfter(
                newArticleTime, newArticleTime
        );
    }

    @Override
    public Page<Article> getArticlesPageByText(String text, Pageable pageable) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().caseSensitive(false).matching(text);
        return articleRepository.findAllBy(criteria, pageable);
    }
}
