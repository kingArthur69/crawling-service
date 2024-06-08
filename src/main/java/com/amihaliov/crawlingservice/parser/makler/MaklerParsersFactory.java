package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.IParsersFactory;
import com.amihaliov.crawlingservice.parser.ParserType;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class MaklerParsersFactory implements IParsersFactory {
    
    private Map<ParserType, IParser> parserMap = Map.of(
            ParserType.MAIN, new MaklerMainParserImpl(),
            ParserType.ARTICLE, new MaklerArticleParserImpl(), 
            ParserType.LISTING, new MaklerListingParserImpl()
    );

    @Override
    public IParser createParser(String link) {
        IParser parser;
        if (StringUtils.equalsAny(link, "//makler.md/ru", "//makler.md/categories")) {
            parser = parserMap.get(ParserType.MAIN);
        } else if (StringUtils.contains(link, "//makler.md/ru") && StringUtils.contains(link, "/an/")) {
            parser = parserMap.get(ParserType.ARTICLE);
        } else if (StringUtils.contains(link, "makler.md")) {
            parser = parserMap.get(ParserType.LISTING);
        } else {
            throw new IllegalArgumentException("Unknown Page Type for Link: " + link);
        }
        return parser;
    }
}
