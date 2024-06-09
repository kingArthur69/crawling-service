package com.amihaliov.crawlingservice.repository;

import com.amihaliov.crawlingservice.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {

}
