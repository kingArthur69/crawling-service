package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.Price;
import com.amihaliov.crawlingservice.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

@Slf4j
@Service
public class ParsingServiceImpl implements IParsingService {

    public static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("dd MMMM HH:mm")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(new Locale("ru"));

    @Override
    public List<Article> parse(Document document) {

        try {
            Elements elements = document.select("div.ls-detail article");

            List<Article> articles = new ArrayList<>();
            for (Element element : elements) {
                String url = ParserUtils.getAttr(element, "a.ls-detail_anUrl", ParserUtils.HREF_ATTR);
                String id = StringUtils.substringAfter(url, "transport/cars/an/");
                if (StringUtils.isNotBlank(id)) {
                    Price currentPrice = new Price(ParserUtils.getText(element, "div.ls-detail_anData span.ls-detail_price"));
                    Article article = Article.builder()
                            .id(id)
                            .url("https://makler.md" + url)
                            .imgUrl(ParserUtils.getAttr(element, "div.ls-detail_imgBlock img", "src"))
                            .description(ParserUtils.getText(element, "div.ls-detail_infoBlock p"))
                            .phone(ParserUtils.getText(element, "div.ls-detail_infoBlock > div > span:nth-child(2)"))
                            .currentPrice(currentPrice)
                            .priceHistory(new HashMap<>(Map.of(currentPrice.getValue(), currentPrice)))
                            .lastUpdateTime(parseLastUpdateTime(element))
                            .createTimeStamp(LocalDateTime.now().withSecond(0).withNano(0))
                            .title(ParserUtils.getText(element, "h3.ls-detail_antTitle"))
                            .build();

                    articles.add(article);
                }
            }
            return articles;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private LocalDateTime parseLastUpdateTime(Element element) {
        String timeText = ParserUtils.getText(element, "div.ls-detail_controlsBlock").toLowerCase();
        LocalDateTime time = LocalDateTime.parse(timeText, FORMATTER);

        return time.isAfter(LocalDateTime.now()) ? time.minusYears(1L) : time;
    }
}
