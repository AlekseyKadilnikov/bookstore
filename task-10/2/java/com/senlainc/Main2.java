package com.senlainc;

import static java.lang.Thread.sleep;

public class Main2 {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadOne());
        Thread thread2 = new Thread(new ThreadTwo());
        thread1.start();
        thread2.start();
    }
}

class ThreadOne implements Runnable {
    public static final DisplayName displayName = new DisplayName();
    public static int counter = 30;

    @Override
    public void run() {
        while(counter > 0) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter = counter - 1;
            synchronized (displayName) {
                displayName.display(Thread.currentThread().getName());
                displayName.notify();
                try {
                    displayName.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ThreadTwo implements Runnable {
    public synchronized void displayName() {
        System.out.println(Thread.currentThread().getName());
    }

    @Override
    public void run() {
        while(ThreadOne.counter > 0) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadOne.counter = ThreadOne.counter - 1;
            synchronized (ThreadOne.displayName) {
                ThreadOne.displayName.display(Thread.currentThread().getName());
                ThreadOne.displayName.notify();
                try {
                    ThreadOne.displayName.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class DisplayName {
    public void display(String message) {
        System.out.println(message);
    }
}