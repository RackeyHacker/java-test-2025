package com.task4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final int seconds = 10;
    private static final int time = seconds * 1000;

    public static void main(String[] args) {

        Thread threadDaemon = new Thread(() -> {
            try {
                while (true) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatted = LocalDateTime.now().format(formatter);
                    System.out.println(formatted);
                    Thread.sleep(time);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadDaemon.setDaemon(true);
        threadDaemon.start();

        try {
            Thread.sleep(10_000_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
