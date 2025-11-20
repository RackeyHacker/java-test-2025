package com.service.config;

import com.annotations.Configuration;
import com.config.annotations.ConfigProperty;

@Configuration
public class BookConfig {

    @ConfigProperty(propertyName = "stale.book.months", type = int.class)
    private int staleBookMonths;
    @ConfigProperty(propertyName = "enable.complete.requests", type = boolean.class)
    private boolean enableCompleteRequests;

    public int getStaleBookMonths() {
        return staleBookMonths;
    }

    public boolean isEnableCompleteRequests() {
        return enableCompleteRequests;
    }
}
