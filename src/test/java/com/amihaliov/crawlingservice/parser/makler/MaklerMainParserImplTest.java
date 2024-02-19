package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MaklerMainParserImplTest {

    private static final Path PATH = Path.of(Path.of("").toAbsolutePath().toString(),
            "src\\test\\resources\\com\\amihaliov\\crawlingservice\\parser\\makler\\MaklerCategories.html");
    private static final IParser PARSER = new MaklerMainParserImpl();

    @Test
    void parse() throws IOException {
        Document document = Jsoup.parse(PATH.toFile());
        ParsingResult result = PARSER.parse(document);
        assertEquals(23, result.getCategories().size());
    }
}