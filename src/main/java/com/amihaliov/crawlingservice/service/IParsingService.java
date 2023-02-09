package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IParsingService {
    List<Article> parse(Document document);
}
