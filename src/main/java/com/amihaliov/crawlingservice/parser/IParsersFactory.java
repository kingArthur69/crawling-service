package com.amihaliov.crawlingservice.parser;

public interface IParsersFactory {

    IParser createParser(String link);
}
