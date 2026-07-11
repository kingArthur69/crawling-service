package com.amihaliov.crawlingservice.scheduler;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.repository.ArticleRepository;
import com.amihaliov.crawlingservice.service.rating.RatingCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RatingScheduler {

    private final ArticleRepository articleRepository;
    private final RatingCalculationService ratingCalculationService;

    private static final int BATCH_SIZE = 200;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    public void scheduleRatingRecalculation() {

        int page = 0;

        LocalDateTime threshold = LocalDateTime.now().minusHours(1);
        Page<Article> articles;

        do {
            articles = articleRepository.findByLastScoredAtBefore(
                    threshold,
                    PageRequest.of(page, BATCH_SIZE)
            );

            for (Article article : articles.getContent()) {
                double score = ratingCalculationService.score(article);

                article.setScore(score);
                article.setLastScoredAt(LocalDateTime.now());
            }

            articleRepository.saveAll(articles.getContent());

            page++;

        } while (articles.hasNext());
    }
}
