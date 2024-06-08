package com.amihaliov.crawlingservice.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.NumberUtils;

@Service
public final class ThymeleafUtils {

    private ThymeleafUtils() {}

    public Integer[] numberSequence(int currentPage, int pages) {
        return NumberUtils.sequence(Math.max(1, currentPage - 4), Math.min(currentPage + 4, pages));
    }

    public String formatArticleUrl(String page, String text, String categoryUrl) {
        StringBuilder sb = new StringBuilder("/articles?page=").append(page);
        if(StringUtils.isNotBlank(text)) {
            sb.append("&text=").append(text);
        }
        if(StringUtils.isNotBlank(categoryUrl)) {
            sb.append("&categoryUrl=").append(categoryUrl);
        }
        return sb.toString();
    }
}
