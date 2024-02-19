package com.amihaliov.crawlingservice.repository;

import com.amihaliov.crawlingservice.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findAllByNameAndMarketplace(String name, String marketplace);
}
