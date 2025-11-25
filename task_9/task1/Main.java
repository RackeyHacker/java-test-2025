package com.task1;

public class Main {

    private static final Object monitor = new Object();

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {

            synchronized (monitor) {
                try {
                    monitor.wait();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println(Thread.currentThread().getName());
            }

        });

        // NEW
        System.out.println(thread.getState());

        // RUNNABLE
        thread.start();
        System.out.println(thread.getState());

        // BLOCKED
        try {
            synchronized (monitor) {
                thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(thread.getState());

        // WAITING
        try {
            thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (monitor) {
            System.out.println(thread.getState());
            monitor.notify();
        }

        //TIMED_WAITING
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(thread.getState());

        // TERMINATED
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(thread.getState());
    }
}
