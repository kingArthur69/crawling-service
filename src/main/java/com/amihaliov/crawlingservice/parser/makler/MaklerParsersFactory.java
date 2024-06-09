package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.IParsersFactory;
import com.amihaliov.crawlingservice.parser.ParserType;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class MaklerParsersFactory implements IParsersFactory {
    
    private static final Map<ParserType, IParser> PARSER_MAP = Map.of(
            ParserType.MAIN, new MaklerMainParserImpl(),
            ParserType.ARTICLE, new MaklerArticleParserImpl(), 
            ParserType.LISTING, new MaklerListingParserImpl()
    );

    @Override
    public IParser createParser(String link) {
        IParser parser;
        if(StringUtils.contains(link, "makler.md")) {
            if (StringUtils.contains(link,  "/categories")) {
                parser = PARSER_MAP.get(ParserType.MAIN);
            } else if (StringUtils.contains(link, "/an/")) {
                parser = PARSER_MAP.get(ParserType.ARTICLE);
            } else {
                parser = PARSER_MAP.get(ParserType.LISTING);
            }
        } else {
            throw new IllegalArgumentException("Unknown Page Type for Link: " + link);
        }

        return parser;
    }
}
