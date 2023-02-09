package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IArticleService {

    List<Article> getArticlesPage(Pageable pageable);

    List<Article> getNewArticles();
}
