package com.amihaliov.crawlingservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Characteristics {

    String name;

    String value;
}
