package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArticleService {

    Page<Article> getArticlesPage(Pageable pageable);

    List<Article> getNewArticles();

    Page<Article> getArticlesPageByText(String text, Pageable pageable);

    Page<Article> getArticlePageByUrlsStartingAndTextCriteria(String url, String text, Pageable pageable);
}
