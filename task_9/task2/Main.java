package com.task2;

import java.util.concurrent.CountDownLatch;

public class Main {

    private static final Object monitor = new Object();
    static boolean flag = true;

    public static void main(String[] args) {

        int threadsCount = 2;
        int index = 5;
        CountDownLatch countDownLatch = new CountDownLatch(threadsCount);

        new Thread(() -> {
            for (int i = 0; i < index; i++) {
                synchronized (monitor) {
                    String name = Thread.currentThread().getName();
                    while (!flag) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(name);
                    flag = false;
                    monitor.notifyAll();
                }
            }
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            for (int i = 0; i < index; i++) {
                synchronized (monitor) {
                    String name = Thread.currentThread().getName();
                    while (flag) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(name);
                    flag = true;
                    monitor.notifyAll();
                }
            }
            countDownLatch.countDown();
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
