package com.senlainc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Main3 {
    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer(producer);
        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

class Producer implements Runnable{

    List<Integer> numbers = new ArrayList<>();
    Random random = new Random();

    public void run(){
        for (int i = 1; i < 50; i++) {
            put();
            try {
                sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void get() {
        while (numbers.size()<1) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        int num = numbers.get(numbers.size() - 1);
        System.out.println("Потребитель потребил число " + num);
        numbers.remove(numbers.size() - 1);
        System.out.println("Буфер: " + numbers.toString());
        notify();
    }
    public synchronized void put() {
        while (numbers.size() >= 6) {
            try {
                wait();
            }
            catch (InterruptedException e) {
            }
        }
        int num = random.nextInt(100);
        numbers.add(num);
        System.out.println("Производитель добавил число " + num);
        System.out.println("Буфер: " + numbers.toString());
        notify();
    }
}

class Consumer implements Runnable{

    private Producer producer;

    public Consumer(Producer producer) {
        this.producer = producer;
    }

    public void run(){
        for (int i = 1; i < 50; i++) {
            producer.get();
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}