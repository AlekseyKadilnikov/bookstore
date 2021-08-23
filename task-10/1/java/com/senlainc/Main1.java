package com.senlainc;

import static java.lang.Thread.sleep;

public class Main1 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new Thread1());
        Thread thread2 = new Thread(new Thread2());
        System.out.println(thread2.getName() + ": " + thread2.getState()); // new
        thread1.start();
        thread2.start();
        sleep(1000);
        System.out.println(thread2.getName() + ": " + thread2.getState()); // blocked
        sleep(2000);
        System.out.println(thread2.getName() + ": " + thread2.getState()); // waiting
        Integer num = Thread2.getNum2();
        synchronized (num) {
            num.notify();
        }
        sleep(1000);
        System.out.println(thread2.getName() + ": " + thread2.getState()); // timed_waiting
        sleep(2000);
        System.out.println(thread2.getName() + ": " + thread2.getState()); // terminated
    }
}

class Thread1 implements Runnable {

    private static Integer num1 = 0;

    public static void increment() throws InterruptedException {
        synchronized (num1) {
            Thread.sleep(1000);
            num1 = num1 + 1;
        }
    }

    @Override
    public void run() {
        try {
            increment();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class Thread2 implements Runnable {

    public static Integer num2 = 0;

    public static Integer getNum2() {
        return num2;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": " + Thread.currentThread().getState()); // runnable
        try {
            incrementNumFromThread1();
            synchronized (num2) {
                num2.wait();
            }
            synchronized (num2) {
                num2.wait(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void incrementNumFromThread1() throws InterruptedException {
        Thread1.increment();

    }


}
