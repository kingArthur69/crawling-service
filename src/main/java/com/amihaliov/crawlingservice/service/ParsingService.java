package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.utils.ParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParsingService implements IParsingService {

    private final IParserResolvingService parserResolvingService;

    @Override
    public ParsingResult parse(Document document) {
        String link = ParserUtils.getAttr(document, "link[rel='canonical']", "href");
        IParser parser = parserResolvingService.getParser(link);
        return parser.parse(document);
    }
}
