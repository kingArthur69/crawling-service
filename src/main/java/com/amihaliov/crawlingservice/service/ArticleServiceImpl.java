package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public Page<Article> getArticlePageByUrlsStartingAndTextCriteria(String url, String text, Pageable pageable) {
        Page<Article> articles;

        if(StringUtils.isNotBlank(url) && isSearchByTextPossible(text)) {
            String searchUrl = StringUtils.remove(url, "transnistria/");
            TextCriteria criteria = TextCriteria.forDefaultLanguage().caseSensitive(false).matching(text);
            articles = articleRepository.findAllByUrlStartsWith(searchUrl, criteria, pageable);
        } else if (isSearchByTextPossible(text)) {
            TextCriteria criteria = TextCriteria.forDefaultLanguage().caseSensitive(false).matching(text);
            articles = articleRepository.findAllBy(criteria, pageable);
        } else if (StringUtils.isNotBlank(url)) {
            String searchUrl = StringUtils.remove(url, "transnistria/");
            articles = articleRepository.findByUrlStartsWith(searchUrl, pageable);
        } else {
            articles = articleRepository.findAll(pageable);
        }
        return articles;
    }

    private static boolean isSearchByTextPossible(String trimmedText) {
        int textLength = StringUtils.length(trimmedText);
        return textLength > 2 && textLength < 25;
    }

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
