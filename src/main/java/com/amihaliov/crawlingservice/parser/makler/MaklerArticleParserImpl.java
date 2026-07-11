package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.entity.ArticleDetails;
import com.amihaliov.crawlingservice.entity.Characteristics;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.utils.ParserUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.amihaliov.crawlingservice.parser.utils.ParserUtils.HREF_ATTR;

public class MaklerArticleParserImpl implements IParser {
    public static final Pattern VIEWS_PATTERN = Pattern.compile(".*:\\s*(\\d+)\\s*$");

    @Override
    public ParsingResult parse(Document document) {
        ArticleDetails articleDetails = ArticleDetails.builder()
                .articleId(parseArticleId(document))
                .viewsCount(parseViews(document))
                .user(ParserUtils.getText(document, "a.item_sipmleUser"))
                .userArticles(Integer.parseInt(ParserUtils.getText(document, "div.opened li b")))
                .imgUrls(parseImgUrls(document))
                .characteristics(parseCharacteristics(document))
                .build();

        ParsingResult parsingResult = new ParsingResult();
        parsingResult.setArticleDetails(articleDetails);
        return parsingResult;
    }

    private static String parseArticleId(Document document) {
        return Objects.requireNonNull(document.selectFirst("input#an_id")).val();
    }

    private List<Characteristics> parseCharacteristics(Document document) {
        List<Characteristics> result = document.select("ul.itemtable.box-columns li")
                .stream()
                .map(this::buildCharacteristic)
                .collect(Collectors.toList());

        return !result.isEmpty() ? result : null;
    }

    private Characteristics buildCharacteristic(Element element) {
        return Characteristics.builder()
                .name(ParserUtils.getText(element, "div.fields"))
                .value(ParserUtils.getText(element, "div.values"))
                .build();
    }

    Integer parseViews(Document document) {
        Elements elements = document.select("div.item_title_info span");
        for (Element element : elements) {
            Matcher matcher = VIEWS_PATTERN.matcher(element.text());

            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return null;
    }

    private List<String> parseImgUrls(Document document) {
        List<String> urls = document.select("div.itmedia a.fancybox-disaproved-img")
                .stream()
                .map(element -> element.attr(HREF_ATTR))
                .collect(Collectors.toList());

        return !urls.isEmpty() ? urls : null;
    }

}
