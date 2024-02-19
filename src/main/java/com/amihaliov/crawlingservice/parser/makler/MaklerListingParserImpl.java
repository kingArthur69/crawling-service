package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.entity.Article;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.entity.Price;
import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.utils.ParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
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
public class MaklerListingParserImpl implements IParser {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("dd MMMM HH:mm")
            .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
            .toFormatter(new Locale("ru"));

    private static final String NEXT_PAGE_SELECTOR = "li a.paginator_pageLink.paginator_nextPage";

    @Override
    public ParsingResult parse(Document document) {
        try {
            Elements elements = document.select("div.ls-detail article");

            List<Article> articles = new ArrayList<>();
            for (Element element : elements) {
                String url = ParserUtils.getAttr(element, "a.ls-detail_anUrl", ParserUtils.HREF_ATTR);
                if (StringUtils.isNotBlank(url)) {
                    Price currentPrice = new Price(element.select("div.ls-detail_anData span.ls-detail_price, div.priceBox span").text());
                    Article article = Article.builder()
                            .id(url)
                            .url("https://makler.md" + url)
                            .imgUrl(parseImgUrl(element))
                            .description(element.select("div.ls-detail_infoBlock p, div.subfir").text())
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

            ParsingResult result = new ParsingResult();
            result.setArticles(articles);
            result.setNextPageUrl(getNextPageUrl(document));

            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String parseImgUrl(Element element) {
        Elements img = element.select("div.ls-detail_imgBlock img");
        String imgUrl = img.attr("src");
        if(StringUtils.equalsAny(imgUrl,"", "/layouts/default/img/photo_placeholder_120x90.png")) {
            imgUrl = img.attr("data-src");
        }
        return imgUrl;
    }

    private LocalDateTime parseLastUpdateTime(Element element) {
        String timeText = ParserUtils.getText(element, "div.ls-detail_controlsBlock div.ls-detail_time").toLowerCase();
        LocalDateTime time = LocalDateTime.parse(timeText, FORMATTER);

        return time.isAfter(LocalDateTime.now()) ? time.minusYears(1L) : time;
    }

    private String getNextPageUrl(Document document) {
        String link = "https:" +  ParserUtils.getAttr(document, "link[rel='canonical']", ParserUtils.HREF_ATTR);
        String nextPage = ParserUtils.getAttr(document, NEXT_PAGE_SELECTOR, ParserUtils.HREF_ATTR);
        return StringUtils.isNotBlank(nextPage) ? StringUtils.substringBefore(link, "?") + nextPage : null;
    }
}
