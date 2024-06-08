package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.parser.IParser;

public interface IParserResolvingService {

    IParser getParser(String link);
}
