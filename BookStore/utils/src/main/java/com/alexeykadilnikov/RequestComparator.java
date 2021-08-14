package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Request;

import java.util.Comparator;

public class RequestComparator {
    public static final Comparator<Request> NameComparatorAscending = (r1, r2) -> r1.getName().compareTo(r2.getName());
    public static final Comparator<Request> NameComparatorDescending = (r1, r2) -> r2.getName().compareTo(r1.getName());
    public static final Comparator<Request> AmountComparatorAscending = (r1, r2) -> r1.getCount() - r2.getCount();
    public static final Comparator<Request> AmountComparatorDescending = (r1, r2) -> r2.getCount() - r1.getCount();
}
