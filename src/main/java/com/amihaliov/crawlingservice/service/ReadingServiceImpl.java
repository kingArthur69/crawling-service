package com.amihaliov.crawlingservice.service;

import com.amihaliov.crawlingservice.entity.Crawl;
import com.amihaliov.crawlingservice.repository.CrawlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReadingServiceImpl implements IReadingService {

    private final CrawlRepository crawlRepository;
    @Override
    public List<Crawl> findCrawls() {
        Sort sortBy = Sort.by(Sort.Order.desc("startTimeStamp"));
        Pageable pageable = PageRequest.of(0, 50, sortBy);
        return crawlRepository.findAll(pageable).getContent();
    }
}
