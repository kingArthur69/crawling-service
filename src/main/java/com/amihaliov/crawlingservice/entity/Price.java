package com.amihaliov.crawlingservice.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Price {

    public Price(String value) {
        this.value = value;
    }

    LocalDateTime creationTime = LocalDateTime.now().withNano(0);

    String value;

}
