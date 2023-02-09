package com.amihaliov.crawlingservice.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Price {

    public Price(String price) {
        this.price = price;
    }

    LocalDateTime creationTime = LocalDateTime.now();

    private String price;

}
