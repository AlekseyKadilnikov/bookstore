package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;

public class Utils {
    public static void showBookArray(Book[] books) {
        for (Book book : books) {
            System.out.println("name = " + book.getName() +
                    ", price = " + book.getPrice() +
                    ", date = " + book.getPublicationYear() +
                    ", available = " + book.isAvailable() +
                    ", dateOfReceipt = " + book.getDateOfReceipt());
        }
        System.out.println();
    }

    public static void showOrderArray(Order[] orders) {
        for (Order order : orders) {
            System.out.println("id = " + order.getId() +
                    ", price = " + order.getPrice() +
                    ", date = " + order.getExecutionDate() +
                    ", status = " + order.getStatus());
        }
        System.out.println();
    }

    public static void showRequestArray(Request[] requests) {
        for (Request request : requests) {
            System.out.println("id = " + request.getId() +
                    ", book = " + request.getBook() +
                    ", user = " + request.getUser() +
                    ", amount = " + request.getAmount());
        }
        System.out.println();
    }
}
