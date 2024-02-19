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

import static org.junit.jupiter.api.Assertions.*;

class MaklerListingParserImplTest {

    private static final Path PATH = Path.of(Path.of("").toAbsolutePath().toString(),
            "src\\test\\resources\\com\\amihaliov\\crawlingservice\\parser\\makler\\MaklerListing.html");
    private static final IParser PARSER = new MaklerListingParserImpl();

    @Test
    void parse() throws IOException {
        Document document = Jsoup.parse(PATH.toFile());
        ParsingResult result = PARSER.parse(document);
        List<Article> articles = result.getArticles();
        assertEquals(96, articles.size());
        Article article = articles.get(0);
        assertAll(
                () -> assertEquals("/ru/products-for-children/miscellaneous/an/45445", article.getId()),
                () -> assertEquals("https://makler.md/ru/products-for-children/miscellaneous/an/45445", article.getUrl()),
                () -> assertEquals("https://media.makler.md/production/an/thumb/000/030/993/000030993999.jpg", article.getImgUrl()),
                () -> assertEquals("Скейтборды Penny с прорезом для руки новая модель-390 лей. алюминиевая подвеска, колеса ПУ, светящиеся колеса, подшипник ABEC-7,", article.getDescription()),
                () -> assertEquals("373-78-803133", article.getPhone()),
                () -> assertEquals("Скейтборды Penny с прорезом для руки новая модель-390 лей.", article.getTitle()),
                () -> assertEquals("390 Lei", article.getCurrentPrice().getValue()),
                () -> assertEquals(LocalDateTime.of(2023,5,21,10,54), article.getLastUpdateTime())
        );
    }
}