package com.alexeykadilnikov.comparator;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;

import java.util.Comparator;

public class BookComparator {
    public static final Comparator<Book> NameComparatorAscending = (b1, b2) -> b1.getName().compareTo(b2.getName());
    public static final Comparator<Book> NameComparatorDescending = (b1, b2) -> b2.getName().compareTo(b1.getName());
    public static final Comparator<Book> DateComparatorAscending = (b1, b2) -> b1.getPublicationYear() - b2.getPublicationYear();
    public static final Comparator<Book> DateComparatorDescending = (b1, b2) -> b2.getPublicationYear() - b1.getPublicationYear();
    public static final Comparator<Book> PriceComparatorAscending = (b1, b2) -> b1.getPrice() - b2.getPrice();
    public static final Comparator<Book> PriceComparatorDescending = (b1, b2) -> b2.getPrice() - b1.getPrice();
    public static final Comparator<Book> ReceiptComparatorAscending =
            (b1, b2) -> b1.getDateOfReceipt().compareTo(b2.getDateOfReceipt());
    public static final Comparator<Book> ReceiptComparatorDescending =
            (b1, b2) -> b2.getDateOfReceipt().compareTo(b1.getDateOfReceipt());
    public static final Comparator<Book> AvailableComparatorAscending = (b1, b2) -> {
        int v2 = b1.isAvailable() ? 1 : 0;
        int v1 = b2.isAvailable() ? 1 : 0;

        return v2 - v1;
    };
    public static final Comparator<Book> AvailableComparatorDescending = (b1, b2) -> {
        int v1 = b1.isAvailable() ? 1 : 0;
        int v2 = b2.isAvailable() ? 1 : 0;

        return v2 - v1;
    };
}
