package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.ParsingResult;
import org.jsoup.nodes.Document;

public interface IParsingService {

    ParsingResult parse(Document document);

}
