package model.config;

import java.io.InputStream;
import java.util.Properties;

public class BookStoreConfig {

    private final int staleBookMonths;
    private final boolean enableCompleteRequests;

    public BookStoreConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Error loading config.properties", e);
        }

        staleBookMonths = Integer.parseInt(props.getProperty("stale.book.months"));
        enableCompleteRequests = Boolean.parseBoolean(props.getProperty("enable.complete.requests"));
    }

    public int getStaleBookMonths() {
        return staleBookMonths;
    }

    public boolean isEnableCompleteRequests() {
        return enableCompleteRequests;
    }
}
