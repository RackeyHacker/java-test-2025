package com.task3;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static final Object monitor = new Object();

    public static void main(String[] args) {

        Thread producer = new Thread(() -> {
            try {
                while (true) {
                    int taskNumber = new Random().nextInt(1, 100_000);
                    queue.add("Task - " + taskNumber);
                    System.out.println("Sent task - " + taskNumber);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String task = queue.take();
                    System.out.println("Got " + task);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        consumer.start();
        producer.start();

        try {
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
