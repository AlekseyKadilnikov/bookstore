package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;

import java.util.Comparator;

public class RequestComparator {
    public static final Comparator<Request> AmountComparatorAscending =
            (r1, r2) -> r1.getAmount() - r2.getAmount();
    public static final Comparator<Request> AmountComparatorDescending =
            (r1, r2) -> r2.getAmount() - r1.getAmount();
}
