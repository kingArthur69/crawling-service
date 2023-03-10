package com.amihaliov.crawlingservice.repository;

import com.amihaliov.crawlingservice.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    Page<Article> findAllBy(TextCriteria textCriteria, Pageable pageable);

    //    @Query(fields = "{ 'id' : 1 }")
    Collection<Article> findDistinctAllByIdIn(Collection<String> ids);

    List<Article> findByUpdateTimeStampNullAndLastUpdateTimeAfterAndCreateTimeStampAfter(
            LocalDateTime updateTime, LocalDateTime createTime
    );
}
