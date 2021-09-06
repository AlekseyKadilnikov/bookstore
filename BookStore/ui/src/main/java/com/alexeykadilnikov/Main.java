package com.alexeykadilnikov;

import com.alexeykadilnikov.config.SpringConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        BookStore bookStore = context.getBean("Bookstore", BookStore.class);
        bookStore.start();
    }
}
