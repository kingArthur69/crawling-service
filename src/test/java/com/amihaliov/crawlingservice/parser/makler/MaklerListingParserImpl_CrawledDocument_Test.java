package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaklerListingParserImpl_CrawledDocument_Test {

    private static final Path PATH = Path.of(Path.of("").toAbsolutePath().toString(),
            "src\\test\\resources\\com\\amihaliov\\crawlingservice\\parser\\makler\\MaklerListingCrawledDocument.html");
    private static final IParser PARSER = new MaklerListingParserImpl();

    @Test
    void parse() throws IOException {
        Document document = Jsoup.parse(PATH.toFile());
        ParsingResult result = PARSER.parse(document);
        List<Article> articles = result.getArticles();
        assertEquals(96, articles.size());
        Article article = articles.get(0);
        assertAll(
                () -> assertEquals("/ru/computers-and-office-equipment/desktops/an/52939", article.getId()),
                () -> assertEquals("https://makler.md/ru/computers-and-office-equipment/desktops/an/52939", article.getUrl()),
                () -> assertEquals("", article.getImgUrl()),
                () -> assertEquals("Продам компьютер (системный блок + монитор - 19д., клавиатура). Жесткий диск новый. Комп. Куплен в 2010г ", article.getDescription()),
                () -> assertEquals("373-77-792300", article.getPhone()),
                () -> assertEquals("Продам компьютер", article.getTitle()),
                () -> assertEquals("1 400 руб", article.getCurrentPrice().getValue()),
                () -> assertEquals(LocalDateTime.of(2024,5,21,12,39), article.getLastUpdateTime())
        );

        Article article2 = articles.get(1);
        assertAll(
                () -> assertEquals("/ru/computers-and-office-equipment/tablets/an/17382", article2.getId()),
                () -> assertEquals("https://makler.md/ru/computers-and-office-equipment/tablets/an/17382", article2.getUrl()),
                () -> assertEquals("https://media.makler.md/production/an/thumb/000/058/454/000058454104.jpg", article2.getImgUrl()),
                () -> assertEquals("Айпад 8 новый, не распечатанный. ", article2.getDescription()),
                () -> assertEquals("373-77-771923", article2.getPhone()),
                () -> assertEquals("Айпад 8, новый", article2.getTitle()),
                () -> assertEquals("290 $", article2.getCurrentPrice().getValue()),
                () -> assertEquals(LocalDateTime.of(2024,5,21,12,39), article2.getLastUpdateTime())
        );
    }
}