package com.senlainc;

import java.time.LocalDateTime;

import static java.lang.Thread.sleep;

public class Main4 {
    public static void main(String[] args) {
        Thread thread = new Thread(new ServiceThread(1));
        thread.setDaemon(true);
        thread.start();
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ServiceThread implements Runnable {

    int interval;

    public ServiceThread(int interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(interval * 1000);
                System.out.println(LocalDateTime.now());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
