package com.alexeykadilnikov;

import com.alexeykadilnikov.context.Application;
import com.alexeykadilnikov.context.ApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = Application.run("com.alexeykadilnikov");
        BookStore bookStore = context.getBean(BookStore.class);
        bookStore.start();
    }
}
