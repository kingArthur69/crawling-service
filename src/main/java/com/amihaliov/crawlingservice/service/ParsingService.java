package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.makler.MaklerArticleParserImpl;
import com.amihaliov.crawlingservice.parser.makler.MaklerListingParserImpl;
import com.amihaliov.crawlingservice.parser.makler.MaklerMainParserImpl;
import com.amihaliov.crawlingservice.parser.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParsingService implements IParsingService {

    @Override
    public ParsingResult parse(Document document) {

        IParser parser;
        String link = ParserUtils.getAttr(document, "link[rel='canonical']", "href");
        if (StringUtils.equalsAny(link, "//makler.md/ru", "//makler.md/categories")) {
            parser = new MaklerMainParserImpl();
        } else if (StringUtils.contains(link, "//makler.md/ru") && StringUtils.contains(link, "/an/")) {
            parser = new MaklerArticleParserImpl();
        } else if (StringUtils.contains(link, "makler.md")) {
            parser = new MaklerListingParserImpl();
        } else {
            throw new IllegalArgumentException("Unknown Page Type");
        }

        return parser.parse(document);
    }
}
