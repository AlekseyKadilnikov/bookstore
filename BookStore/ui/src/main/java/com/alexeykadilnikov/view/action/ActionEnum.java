package com.alexeykadilnikov.view.action;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.OrderComparator;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestComparator;
import com.alexeykadilnikov.controller.BookController;
import com.alexeykadilnikov.controller.OrderController;
import com.alexeykadilnikov.controller.RequestController;
import com.alexeykadilnikov.controller.UserController;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.utils.UserUtils;
import com.alexeykadilnikov.view.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public enum ActionEnum implements IAction {
    CREATE_USER(() -> {
        UserController userController = UserController.getInstance();
        String username = getStringInput("Enter new username:");

        while (userController.create(username) > 0) {
            username = getStringInput("This username already created. Please try again\nEnter new username:");
        }

        userController.create(username);

        UserUtils.setCurrentUser(userController.getOne(username));
    }),

    SIGN_IN(() -> {
        UserController userController = UserController.getInstance();
        String username = getStringInput("Enter username:");

        while (userController.getOne(username) == null) {
            username = getStringInput("User not found. Please try again:\nEnter username:");
        }

        UserUtils.setCurrentUser(userController.getOne(username));

        List<MenuItem> items = MenuUtils.rootMenu.getMenuItems();
        for(MenuItem item : items) {
            if(item.getTitle().equals("Sign in")) {
                if(UserUtils.getCurrentUser().getUsername().equals("admin")) {
                    item.setNextMenu(MenuUtils.adminMenu);
                }
                else {
                    item.setNextMenu(MenuUtils.customerMenu);
                }
                break;
            }
        }
    }),

    SEARCH(() -> {
        RequestController requestController = RequestController.getInstance();
        String request = getStringInput("Enter request");

        requestController.search(request);
    }),

    SORT_BOOKS_BY_NAME(() -> {
        BookController bookController = BookController.getInstance();

        List<Book> books = bookController.getAll();

        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));

        if(sortType == 0) {
            bookController.sort(books, BookComparator.NameComparatorAscending);
        }
        else {
            bookController.sort(books, BookComparator.NameComparatorDescending);
        }
    }),

    SORT_BOOKS_BY_PRICE(() -> {
        BookController bookController = BookController.getInstance();

        List<Book> books = bookController.getAll();

        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));

        if(sortType == 0) {
            bookController.sort(books, BookComparator.PriceComparatorAscending);
        }
        else {
            bookController.sort(books, BookComparator.PriceComparatorDescending);
        }
    }),

    SORT_ORDERS_BY_PRICE(() -> {
        OrderController orderController = OrderController.getInstance();
        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));
        List<Order> orders = orderController.getAll();
        if(sortType == 0) {
            orderController.sort(orders, OrderComparator.PriceComparatorAscending);
        }
        else {
            orderController.sort(orders, OrderComparator.PriceComparatorDescending);
        }
    }),

    SORT_ORDERS_BY_STATUS(() -> {
        OrderController orderController = OrderController.getInstance();
        int status = Integer.parseInt(getStringInput("Enter status:\n0.New\n1.Success\n2.Canceled"));

        switch (status) {
            case 0:
                System.out.println(orderController.sortByStatus(OrderStatus.NEW).toString());
                break;
            case 1:
                System.out.println(orderController.sortByStatus(OrderStatus.SUCCESS).toString());
                break;
            case 2:
                System.out.println(orderController.sortByStatus(OrderStatus.CANCELED).toString());
                break;
        }
    }),

    SORT_ORDERS_BY_EXEC_DATE(() -> {
        OrderController orderController = OrderController.getInstance();
        List<String> dates = getDateInput();
        int sortName = Integer.parseInt(getStringInput("Enter sort type:\n0.By date\n1.By price"));
        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));

        if(sortName == 0 && sortType == 0) {
            orderController.getCompletedOrdersForPeriod(OrderComparator.DateComparatorAscending, dates.get(0), dates.get(1));
        }
        else if(sortName == 0 && sortType == 1){
            orderController.getCompletedOrdersForPeriod(OrderComparator.DateComparatorDescending, dates.get(0), dates.get(1));
        }
        else if(sortName == 1 && sortType == 0) {
            orderController.getCompletedOrdersForPeriod(OrderComparator.PriceComparatorAscending, dates.get(0), dates.get(1));
        }
        else if(sortName == 1 && sortType == 1) {
            orderController.getCompletedOrdersForPeriod(OrderComparator.PriceComparatorDescending, dates.get(0), dates.get(1));
        }
    }),

    EARNED_MONEY(() -> {
        OrderController orderController = OrderController.getInstance();
        List<String> dates = getDateInput();
        orderController.showEarnedMoneyForPeriod(dates.get(0), dates.get(1));
    }),

    COMPL_ORDERS_COUNT(() -> {
        OrderController orderController = OrderController.getInstance();
        List<String> dates = getDateInput();
        orderController.showCompletedOrdersCountForPeriod(dates.get(0), dates.get(1));
    }),

    GET_ORDER_DETAIL(() -> {
        OrderController orderController = OrderController.getInstance();
        int orderId = Integer.parseInt(getStringInput("Enter order id:"));
        orderController.showOne(orderId);
    }),

    NEW_ORDER(() -> {
        BookController bookController = BookController.getInstance();
        List<Book> books = bookController.getAll();
        bookController.sort(books, BookComparator.PriceComparatorAscending);

        String bookIds = getStringInput("Enter book ids (example: 1 1 5)");
        String[] stringIds = bookIds.split(" ");
        List<Integer> intIds = new ArrayList<>();
        for(String id : stringIds) {
            intIds.add(Integer.parseInt(id));
        }

        OrderController orderController = OrderController.getInstance();
        orderController.create(intIds);
    }),

    GET_USER_ORDERS(() -> {
        UserController userController = UserController.getInstance();
        userController.getOrders(UserUtils.getCurrentUser());
    }),

    GET_ADMIN_ORDERS(() -> {
        OrderController orderController = OrderController.getInstance();
        orderController.showAll();
    }),

    CANCEL_ORDER(() -> {
        OrderController orderController = OrderController.getInstance();
        int orderId = Integer.parseInt(getStringInput("Enter order id:"));
        orderController.cancel(orderId);
    }),

    SHOW_BOOK_DESCRIPTION(() -> {
        BookController bookController = BookController.getInstance();
        int bookId = Integer.parseInt(getStringInput("Enter book id:"));
        bookController.showDescription(bookId);
    }),

    SHOW_REQUESTS_FOR_BOOK(() -> {
        RequestController requestController = RequestController.getInstance();
        int bookId = Integer.parseInt(getStringInput("Enter book id:"));

        int sortName = Integer.parseInt(getStringInput("Enter sort type:\n0.By books count\n1.By name"));
        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));

        if(sortName == 0 && sortType == 0) {
            requestController.sort(bookId, RequestComparator.AmountComparatorAscending);
        }
        else if(sortName == 0 && sortType == 1){
            requestController.sort(bookId, RequestComparator.AmountComparatorDescending);
        }
        else if(sortName == 1 && sortType == 0) {
            requestController.sort(bookId, RequestComparator.NameComparatorAscending);
        }
        else if(sortName == 1 && sortType == 1) {
            requestController.sort(bookId, RequestComparator.NameComparatorDescending);
        }
    }),

    SHOW_STALE_BOOKS(() -> {
        BookController bookController = BookController.getInstance();
        int months = Integer.parseInt(getStringInput("Enter count of months:"));

        List<Book> staleBooks = bookController.getStaleBooks(months);

        int sortName = Integer.parseInt(getStringInput("Enter sort type:\n0.By date of receipt\n1.By price"));
        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));

        if(sortName == 0 && sortType == 0) {
            bookController.sort(staleBooks, BookComparator.ReceiptComparatorAscending);
        }
        else if(sortName == 0 && sortType == 1){
            bookController.sort(staleBooks, BookComparator.ReceiptComparatorDescending);
        }
        else if(sortName == 1 && sortType == 0) {
            bookController.sort(staleBooks, BookComparator.PriceComparatorAscending);
        }
        else if(sortName == 1 && sortType == 1) {
            bookController.sort(staleBooks, BookComparator.PriceComparatorDescending);
        }
    });

    private final IAction action;

    ActionEnum(IAction action) {
        this.action = action;
    }

    @Override
    public void execute() {
        action.execute();
    }

    private static String getStringInput(String param) {
        Scanner sc = new Scanner(System.in);
        String str = "";
        System.out.println(param);
        str = sc.nextLine();

        return str;
    }

    private static List<String> getDateInput() {
        String dateAfter = getStringInput("Enter first date (yyyy-mm-dd):");
        String dateBefore = getStringInput("Enter second date (yyyy-mm-dd):");

        List<String> dates = new ArrayList<>();
        dates.add(dateAfter);
        dates.add(dateBefore);
        return dates;
    }
}
