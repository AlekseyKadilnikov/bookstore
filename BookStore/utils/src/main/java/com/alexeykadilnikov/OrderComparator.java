package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Order;

import java.util.Comparator;

public class OrderComparator {
    public static final Comparator<Order> DateComparatorAscending =
            (o1, o2) -> o1.getExecutionDate().compareTo(o2.getExecutionDate());
    public static final Comparator<Order> DateComparatorDescending =
            (o1, o2) -> o2.getExecutionDate().compareTo(o1.getExecutionDate());
    public static final Comparator<Order> PriceComparatorAscending =
            (o1, o2) -> o1.getTotalPrice() - o2.getTotalPrice();
    public static final Comparator<Order> PriceComparatorDescending =
            (o1, o2) -> o2.getTotalPrice() - o1.getTotalPrice();
    public static final Comparator<Order> StatusComparatorAscending =
            (o1, o2) -> o1.getStatus().getStatusCode() - o2.getStatus().getStatusCode();
    public static final Comparator<Order> StatusComparatorDescending =
            (o1, o2) -> o2.getStatus().getStatusCode() - o1.getStatus().getStatusCode();
}
