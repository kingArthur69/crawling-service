package com.amihaliov.crawlingservice.parser;


import com.amihaliov.crawlingservice.entity.ParsingResult;
import org.jsoup.nodes.Document;

public interface IParser {

    ParsingResult parse(Document document);

}
