package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.parser.makler.MaklerListingParserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserResolvingServiceImplTest {

    private final ParserResolvingServiceImpl parserResolvingService = new ParserResolvingServiceImpl();

    @Test
    void getParserForIncorrectLink() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parserResolvingService.getParser("testUrl"));

        assertEquals("Site is not supported: testUrl", exception.getMessage());
    }

    @Test
    void getParser() {
        assertInstanceOf(MaklerListingParserImpl.class, parserResolvingService.getParser("makler.md"));
    }
}