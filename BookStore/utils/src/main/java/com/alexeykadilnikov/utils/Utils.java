package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;

import java.util.List;

public class Utils {
    public static void showBookArray(List<Book> books) {
        if(books.isEmpty()) {
            System.out.println("Books not found");
            return;
        }
        for (Book book : books) {
            System.out.println(book.toString());
//            System.out.println("name = " + book.getName() +
//                    ", price = " + book.getPrice() +
//                    ", date = " + book.getPublicationYear() +
//                    ", count = " + book.getCount() +
//                    ", dateOfReceipt = " + book.getDateOfReceipt());
        }
    }

    public static void showOrderArray(List<Order> orders) {
        if(orders.isEmpty()) {
            System.out.println("Orders not found");
            return;
        }
        for (Order order : orders) {
            System.out.println(order.toString());
//            System.out.println("id = " + order.getId() +
//                    ", price = " + order.getPrice() +
//                    ", executionDate = " + order.getExecutionDate() +
//                    ", status = " + order.getStatus());
        }
    }

    public static void showRequestArray(List<Request> requests) {
        for (Request request : requests) {
            System.out.println("name = " + request.getName() +
                    ", count = " + request.getCount());
        }
    }
}
