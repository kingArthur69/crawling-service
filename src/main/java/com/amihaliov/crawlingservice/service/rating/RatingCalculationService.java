package com.amihaliov.crawlingservice.service.rating;

import com.amihaliov.crawlingservice.entity.Article;

public interface RatingCalculationService {

    double score(Article article);
}
