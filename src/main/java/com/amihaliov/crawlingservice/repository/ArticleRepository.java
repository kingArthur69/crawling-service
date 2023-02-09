package com.amihaliov.crawlingservice.repository;

import com.amihaliov.crawlingservice.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

//    @Query(fields = "{ 'id' : 1 }")
    Collection<Article> findDistinctAllByIdIn(Collection<String> ids);
}
