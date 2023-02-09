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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ParsingServiceImpl implements IParsingService {
    @Override
    public List<Article> parse(Document document) {

        try {
            Elements elements = document.select("div.ls-detail article");

            List<Article> articles = new ArrayList<>();
            for (Element element : elements) {
                String url = ParserUtils.getAttr(element, "a.ls-detail_anUrl", ParserUtils.HREF_ATTR);
                Price price = new Price(ParserUtils.getText(element, "div.ls-detail_anData span.ls-detail_price"));
                Article article = Article.builder()
                        .id(StringUtils.substringAfter(url, "transport/cars/an/"))
                        .url(url) // TODO add https/makeler to make full url
                        .imgUrl(ParserUtils.getAttr(element, "div.ls-detail_imgBlock img", "src"))
                        .description(ParserUtils.getText(element, "div.ls-detail_infoBlock p"))
                        .phone(ParserUtils.getText(element, "div.ls-detail_infoBlock > div > span:nth-child(2)"))
                        .price(new HashSet<>(Set.of(price)))
                        .lastUpdateTime(ParserUtils.getText(element, "div.ls-detail_controlsBlock"))
                        .createTimeStamp(LocalDateTime.now())
                        .title(ParserUtils.getText(element, "h3.ls-detail_antTitle"))
                        .build();

                if (StringUtils.isNotBlank(article.getId())) {
                    articles.add(article);
                }
            }
            return articles;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
