package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.entity.Request;

import java.time.LocalDate;
import java.util.List;

public class QueryBuilder {

    private QueryBuilder() {
    }

    public static String sortBooksByName(int mode) {
        StringBuilder hql = new StringBuilder("from Book order by name ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortBooksByPrice(int mode) {
        StringBuilder hql = new StringBuilder("from Book order by price ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortBooksByPublicationYear(int mode) {
        StringBuilder hql = new StringBuilder("from Book order by publicationYear ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortBooksByCount(int mode) {
        StringBuilder hql = new StringBuilder("from Book b order by b.count ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String getStaleBooksByDate(int mode, int months) {
        LocalDate date = LocalDate.now().minusMonths(months);
        StringBuilder hql = new StringBuilder("from Book where dateOfReceipt < ");
        hql.append(date);
        hql.append(" order by dateOfReceipt ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String getStaleBooksByPrice(int mode, int months) {
        LocalDate date = LocalDate.now().minusMonths(months);
        StringBuilder hql = new StringBuilder("from Book where dateOfReceipt < ");
        hql.append(date);
        hql.append(" order by price ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortOrdersByPrice(int mode) {
        StringBuilder hql = new StringBuilder("from Order order by totalPrice ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortOrdersByExecDate(int mode) {
        StringBuilder hql = new StringBuilder("from Order order by executionDate ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortOrdersByExecDateForPeriodByDate(String startDate, String endDate, int mode) {
        StringBuilder hql = new StringBuilder("from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "' order by executionDate ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String sortOrdersByExecDateForPeriodByPrice(String startDate, String endDate, int mode) {
        StringBuilder hql = new StringBuilder("from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "' order by price ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String getCompleteOrdersForPeriod(String startDate, String endDate) {
        return "from Order where executionDate >= '" + startDate + "' and executionDate <= '" + endDate + "'";
    }

    public static String sortByStatus(OrderStatus status) {
        return "from Order where status = " + status.getStatusCode();
    }

    public static String getRequestsForBookSortedByCount(long bookId, int mode) {
        StringBuilder hql = new StringBuilder("select distinct r from Book as b inner join b.requests as r on b.id = " + bookId + " order by r.count ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }

    public static String getRequestsForBookSortedByName(long bookId, int mode) {
        StringBuilder hql = new StringBuilder("select distinct r from Book as b inner join b.requests as r on b.id = " + bookId + " order by r.name ");

        if(mode == 0) {
            hql.append("asc");
        } else {
            hql.append("desc");
        }

        return hql.toString();
    }
}
