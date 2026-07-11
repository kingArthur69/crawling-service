package com.amihaliov.crawlingservice.service.rating;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.ArticleDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class RatingCalculationServiceImpl implements RatingCalculationService {

    public static final double VIEWS_WEIGHT = 0.4;
    public static final double AGE_WEIGHT = 0.6;
    public static final double AUTHOR_WEIGHT = 0.2;

    @Override
    public double score(Article article) {
        ArticleDetails articleDetails = article.getArticleDetails();
        double viewsScore = Math.exp(-articleDetails.getViewsCount() / 100.0);
        double authorScore = 1.0 / (1 + articleDetails.getUserArticles());
        long daysOld = ChronoUnit.DAYS.between(article.getLastUpdateTime(), LocalDateTime.now());
        double ageScore = Math.min(daysOld / 365.0, 1.0);

        return viewsScore * VIEWS_WEIGHT + authorScore * AUTHOR_WEIGHT + ageScore * AGE_WEIGHT;
    }
}
