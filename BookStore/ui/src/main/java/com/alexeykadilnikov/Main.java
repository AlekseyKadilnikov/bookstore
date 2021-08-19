package com.alexeykadilnikov;

import com.alexeykadilnikov.factory.BeanFactory;

public class Main {

    public static void main(String[] args) {
        BookStore bookStore = BeanFactory.getInstance().getBean(BookStore.class);
        bookStore.start();
    }
}
