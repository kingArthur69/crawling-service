package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingServiceImpl implements ISavingService {

    private static final long UPDATE_HOURS_AMOUNT = 10L;
    private final ArticleRepository articleRepository;

    @Override
    public void save(List<Article> articles) {
        try {
            Set<String> ids = articles.stream().map(Article::getId).collect(Collectors.toSet());

            Collection<Article> existingArticles = articleRepository.findDistinctAllByIdIn(ids);

            Set<String> existingIds = existingArticles.stream().map(Article::getId).collect(Collectors.toSet());

            Set<Article> newArticles = articles.stream()
                    .filter(article -> !existingIds.contains(article.getId()))
                    .collect(Collectors.toSet());

            log.info("Saving " + newArticles.size() + " New Articles");
            articleRepository.saveAll(newArticles);

            Map<String, Article> articleMap = articles.stream()
                    .filter(article -> existingIds.contains(article.getId()))
                    .collect(Collectors.toMap(Article::getId, a -> a));

            Set<Article> updateArticles = new HashSet<>();
            for (Article existingArticle : existingArticles) {
                LocalDateTime updateTimeStamp = LocalDateTime.now();
                if (shouldUpdate(existingArticle, updateTimeStamp)) {
                    Article newArticle = articleMap.get(existingArticle.getId());
                    existingArticle.getPrice().add(newArticle.getPrice().stream().findFirst().orElseThrow());
                    existingArticle.setDescription(newArticle.getDescription());
                    existingArticle.setLastUpdateTime(newArticle.getLastUpdateTime());
                    existingArticle.setUpdateTimeStamp(updateTimeStamp);
                    updateArticles.add(existingArticle);
                }
            }

            log.info("Saving " + updateArticles.size() + " Existing Articles");
            articleRepository.saveAll(updateArticles);

        //TODO mesure save time taken to save old and new
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private boolean shouldUpdate(Article existingArticle, LocalDateTime updateTimeStamp) {
        LocalDateTime existingUpdate = existingArticle.getUpdateTimeStamp();
        return existingUpdate == null && ChronoUnit.HOURS.between(existingArticle.getCreateTimeStamp(), updateTimeStamp) > UPDATE_HOURS_AMOUNT
                || existingUpdate != null && ChronoUnit.HOURS.between(existingUpdate, updateTimeStamp) > UPDATE_HOURS_AMOUNT;
    }
}
