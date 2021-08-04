package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;

import java.util.List;

public class Utils {
    public static void showBookArray(Book[] books) {
        for (Book book : books) {
            System.out.println("name = " + book.getName() +
                    ", price = " + book.getPrice() +
                    ", date = " + book.getPublicationYear() +
                    ", count = " + book.getCount() +
                    ", dateOfReceipt = " + book.getDateOfReceipt());
        }
        System.out.println();
    }

    public static void showOrderArray(Order[] orders) {
        for (Order order : orders) {
            System.out.println("id = " + order.getId() +
                    ", price = " + order.getPrice() +
                    ", executionDate = " + order.getExecutionDate() +
                    ", status = " + order.getStatus());
        }
        System.out.println();
    }

    public static void showRequestArray(List<Request> requests) {
        for (Request request : requests) {
            System.out.println("name = " + request.getName() +
                    ", count = " + request.getCount());
        }
        System.out.println();
    }
}
