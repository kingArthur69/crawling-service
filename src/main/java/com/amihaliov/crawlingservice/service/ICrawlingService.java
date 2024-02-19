package com.amihaliov.crawlingservice.service;

import org.jsoup.nodes.Document;

public interface ICrawlingService {

    Document crawl(String url);

}
