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

class MaklerListingParserImpl_EmptyLastUpdateTime_Test {

    private static final Path PATH = Path.of(Path.of("").toAbsolutePath().toString(),
            "src\\test\\resources\\com\\amihaliov\\crawlingservice\\parser\\makler\\MaklerListingEmptyLastUpdateTime.html");
    private static final IParser PARSER = new MaklerListingParserImpl();

    @Test
    void parse() throws IOException {
        Document document = Jsoup.parse(PATH.toFile());
        ParsingResult result = PARSER.parse(document);
        List<Article> articles = result.getArticles();
        assertEquals(96, articles.size());
        Article article = articles.get(0);
        assertAll(
                () -> assertEquals("/ru/construction-and-repair/construction-and-repair/drilling-and-logging/an/42948", article.getId()),
                () -> assertEquals("https://makler.md/ru/construction-and-repair/construction-and-repair/drilling-and-logging/an/42948", article.getUrl()),
                () -> assertEquals("https://media.makler.md/production/an/thumb/000/059/132/000059132549.jpg", article.getImgUrl()),
                () -> assertEquals("Алмазное сверление отверстий под вытяжки, трубы, воздуховоды, приточные клапаны," +
                        " 22 мм 32 мм 40 мм 50 мм 60 мм 70 мм 80 мм 100 мм 105 мм 110 мм 120 мм 125 мм 130 мм *Сверление отверстия больших диаметров" +
                        " 160 мм 170 мм 180 мм 200 мм 250 мм мм 300 мм 320 мм 350 мм 400 мм 450 мм 500 мм * Алмазное с ", article.getDescription()),
                () -> assertEquals("373-79-241676", article.getPhone()),
                () -> assertEquals("Gaurire cu diamant. Taierea cu diamant. Demolare. Алмазное сверление.", article.getTitle()),
                () -> assertEquals("", article.getCurrentPrice().getValue()),
                () -> assertEquals(LocalDateTime.now().withSecond(0).withNano(0), article.getLastUpdateTime())
        );
    }
}