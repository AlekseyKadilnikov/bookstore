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
}
