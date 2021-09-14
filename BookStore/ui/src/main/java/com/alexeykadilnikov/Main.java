package com.alexeykadilnikov;

import com.alexeykadilnikov.context.Application;
import com.alexeykadilnikov.context.ApplicationContext;
import com.alexeykadilnikov.controller.OrderController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = Application.run("com.alexeykadilnikov");
        BookStore bookStore = context.getBean(BookStore.class);
        bookStore.start();
    }
}