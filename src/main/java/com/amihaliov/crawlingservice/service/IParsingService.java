package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IParsingService {

    ParsingResult parse(Document document);

}
