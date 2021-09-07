package com.alexeykadilnikov;

import com.alexeykadilnikov.config.SpringConfig;
import com.alexeykadilnikov.controller.ControllerUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ControllerUtils.setContext(context);
        BookStore bookStore = context.getBean(BookStore.class);
        bookStore.start();
    }
}
