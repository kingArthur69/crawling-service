package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.IParsersFactory;
import com.amihaliov.crawlingservice.parser.makler.MaklerParsersFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ParserResolvingServiceImpl implements IParserResolvingService {

    private static final Map<String, IParsersFactory> FACTORY_MAP = Map.of("makler.md", new MaklerParsersFactory());

    public IParser getParser(String link) {

        IParsersFactory parsersFactory = getFactory(link);

        return parsersFactory.createParser(link);
    }

    private IParsersFactory getFactory(String link) {
        IParsersFactory factory;
        if (StringUtils.contains(link, "makler.md")) {
            factory = FACTORY_MAP.get("makler.md");
        } else {
            throw new IllegalArgumentException("Site is not supported: " + link);
        }
        return factory;
    }
}
