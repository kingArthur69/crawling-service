package com.amihaliov.crawlingservice.parser.utils;

import org.jsoup.nodes.Element;

public final class ParserUtils {

    private ParserUtils() {}

    public static final String HREF_ATTR = "href";

    public static String getAttr(Element element, String cssQuery, String attr) {
        Element first = element.selectFirst(cssQuery);
        return first != null ? first.attr(attr) : "";
    }

    public static String getText(Element element, String cssQuery) {
        Element first = element.selectFirst(cssQuery);
        return first != null ? first.text() : "";
    }
}
