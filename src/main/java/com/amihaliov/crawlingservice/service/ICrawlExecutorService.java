package com.amihaliov.crawlingservice.service;

public interface ICrawlExecutorService {

    void submit(String url);

    void submitForUpdate(String url);

    void submitForFull(String url);
}
