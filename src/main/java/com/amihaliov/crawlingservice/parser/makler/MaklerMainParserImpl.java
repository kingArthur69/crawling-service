package com.amihaliov.crawlingservice.parser.makler;

import com.amihaliov.crawlingservice.entity.Category;
import com.amihaliov.crawlingservice.entity.ParsingResult;
import com.amihaliov.crawlingservice.parser.IParser;
import com.amihaliov.crawlingservice.parser.utils.ParserUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class MaklerMainParserImpl implements IParser {

    private static final String MAKLER_MD = "makler.md";
    private static final String MAIN_CATEGORY_SELECTOR = "div.map h2 a";
    private static final String ALL_CATEGORY_SELECTOR = "div.map a";
    private static final String URL_PREFIX = "https://makler.md";
    private static final String REFERENCE_CATEGORY_SELECTOR = "div.label-ascii-arrow";

    @Override
    public ParsingResult parse(Document document) {
        Map<String, Category> categoryMap = new HashMap<>();

        for (Element mainCategory : document.select(MAIN_CATEGORY_SELECTOR)) {
            Category category = parseCategory(mainCategory);
            category.setSubcategories(new ArrayList<>());

            categoryMap.put(category.getUrl(), category);
        }

        Elements allCategories = document.select(ALL_CATEGORY_SELECTOR);

        Element firstMainCategory = allCategories.get(0);
        if (firstMainCategory != null && !categoryMap.isEmpty()) {
            Category mainCategory = categoryMap.get(parseCategory(firstMainCategory).getUrl());

            for (int i = 1; i < allCategories.size(); i++) {
                Element categoryElement = allCategories.get(i);

                if(categoryElement.selectFirst(REFERENCE_CATEGORY_SELECTOR) != null) {
                    continue;
                }

                Category subCategory = parseCategory(categoryElement);
                String url = subCategory.getUrl();
                if (categoryMap.containsKey(url)) {
                    mainCategory = categoryMap.get(url);
                } else {
                    mainCategory.getSubcategories().add(subCategory);
                }
            }
        }

        ParsingResult result = new ParsingResult();
        result.setCategories(new ArrayList<>(categoryMap.values()));
        return result;
    }

    private Category parseCategory(Element element) {
        Category category = new Category();
        category.setMarketplace(MAKLER_MD);
        category.setUrl(URL_PREFIX + element.attr(ParserUtils.HREF_ATTR));
        category.setName(element.text());
        return category;
    }
}
