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
import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.utils.StringUtils;
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

    SET_ORDER_STATUS(() -> {
        OrderController orderController = OrderController.getInstance();
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        int statusTypeNum = getNumber("Enter status number:\n0.New\n1.Success\n2.Canceled", 2);
        switch (statusTypeNum) {
            case 0:
                orderController.setStatus(orderId, OrderStatus.NEW);
                break;
            case 1:
                orderController.setStatus(orderId, OrderStatus.SUCCESS);
                break;
            case 2:
                orderController.setStatus(orderId, OrderStatus.CANCELED);
                break;
        }
    }),

    SORT_BOOKS_BY_NAME(() -> {
        BookController bookController = BookController.getInstance();

        List<Book> books = bookController.getAll();

        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortTypeNum == 0) {
            bookController.sort(books, BookComparator.NameComparatorAscending);
        }
        else {
            bookController.sort(books, BookComparator.NameComparatorDescending);
        }
    }),

    SORT_BOOKS_BY_PRICE(() -> {
        BookController bookController = BookController.getInstance();
        List<Book> books = bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            bookController.sort(books, BookComparator.PriceComparatorAscending);
        }
        else {
            bookController.sort(books, BookComparator.PriceComparatorDescending);
        }
    }),

    WRITE_OFF_BOOK(() -> {
        BookController bookController = BookController.getInstance();
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        bookController.writeOff(bookId);
    }),

    ADD_BOOK(() -> {
        BookController bookController = BookController.getInstance();
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        int count = getNumber("Enter count of books:", Integer.MAX_VALUE);
        bookController.addBook(bookId, count);
    }),

    SORT_ORDERS_BY_PRICE(() -> {
        OrderController orderController = OrderController.getInstance();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending:", 1);
        List<Order> orders = orderController.getAll();
        if(sortTypeNum == 0) {
            orderController.sort(orders, OrderComparator.PriceComparatorAscending);
        }
        else {
            orderController.sort(orders, OrderComparator.PriceComparatorDescending);
        }
    }),

    SORT_ORDERS_BY_STATUS(() -> {
        OrderController orderController = OrderController.getInstance();
        int sortTypeNum = getNumber("Enter status number:\n0.New\n1.Success\n2.Canceled", 2);

        switch (sortTypeNum) {
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

        int sortNameNum = getNumber("Enter sort type:\n0.By date\n1.By price", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortNameNum == 0 && sortTypeNum == 0) {
            orderController.getCompletedOrdersForPeriod(OrderComparator.DateComparatorAscending, dates.get(0), dates.get(1));
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            orderController.getCompletedOrdersForPeriod(OrderComparator.DateComparatorDescending, dates.get(0), dates.get(1));
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            orderController.getCompletedOrdersForPeriod(OrderComparator.PriceComparatorAscending, dates.get(0), dates.get(1));
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            orderController.getCompletedOrdersForPeriod(OrderComparator.PriceComparatorDescending, dates.get(0), dates.get(1));
        }
    }),

    SORT_BY_EXEC_DATE(() -> {
        OrderController orderController = OrderController.getInstance();
        List<Order> orders = orderController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            orderController.sort(orders, OrderComparator.DateComparatorAscending);
        }
        else {
            orderController.sort(orders, OrderComparator.DateComparatorDescending);
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
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        orderController.showOne(orderId);
    }),

    SORT_BY_COUNT(() -> {
        BookController bookController = BookController.getInstance();
        List<Book> books = bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            bookController.sort(books, BookComparator.AvailableComparatorAscending);
        }
        else {
            bookController.sort(books, BookComparator.AvailableComparatorDescending);
        }
    }),

    SORT_BOOKS_BY_DATE(() -> {
        BookController bookController = BookController.getInstance();
        List<Book> books = bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            bookController.sort(books, BookComparator.DateComparatorAscending);
        }
        else {
            bookController.sort(books, BookComparator.DateComparatorDescending);
        }
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
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        orderController.cancel(orderId);
    }),

    SHOW_BOOK_DESCRIPTION(() -> {
        BookController bookController = BookController.getInstance();
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        bookController.showDescription(bookId);
    }),

    SHOW_REQUESTS_FOR_BOOK(() -> {
        RequestController requestController = RequestController.getInstance();
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        int sortNameNum = getNumber("Enter number of sort type:\n0.By books count\n1.By name", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortNameNum == 0 && sortTypeNum == 0) {
            requestController.sort(bookId, RequestComparator.AmountComparatorAscending);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            requestController.sort(bookId, RequestComparator.AmountComparatorDescending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            requestController.sort(bookId, RequestComparator.NameComparatorAscending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            requestController.sort(bookId, RequestComparator.NameComparatorDescending);
        }
    }),

    SHOW_STALE_BOOKS(() -> {
        BookController bookController = BookController.getInstance();
        int months = getNumber("Enter count of months:", Integer.MAX_VALUE);
        List<Book> staleBooks = bookController.getStaleBooks(months);
        int sortNameNum = getNumber("Enter sort type:\n0.By date of receipt\n1.By price", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortNameNum == 0 && sortTypeNum == 0) {
            bookController.sort(staleBooks, BookComparator.ReceiptComparatorAscending);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            bookController.sort(staleBooks, BookComparator.ReceiptComparatorDescending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            bookController.sort(staleBooks, BookComparator.PriceComparatorAscending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            bookController.sort(staleBooks, BookComparator.PriceComparatorDescending);
        }
    }),

    IMPORT_USERS(() -> {
        UserController userController = UserController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        userController.importUsers(path);
    }),

    EXPORT_USERS(() -> {
        UserController userController = UserController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter user id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        userController.exportUsers(path, ids);
    }),

    IMPORT_BOOKS(() -> {
        BookController bookController = BookController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        bookController.importBooks(path);
    }),

    EXPORT_BOOKS(() -> {
        BookController bookController = BookController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter book id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        bookController.exportBooks(path, ids);
    }),

    IMPORT_ORDERS(() -> {
        OrderController orderController = OrderController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        orderController.importOrders(path);
    }),

    EXPORT_ORDERS(() -> {
        OrderController orderController = OrderController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter order id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        orderController.exportOrders(path, ids);
    }),

    IMPORT_REQUESTS(() -> {
        RequestController requestController = RequestController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        requestController.importRequests(path);
    }),

    EXPORT_REQUESTS(() -> {
        RequestController requestController = RequestController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter book id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        requestController.exportRequests(path, ids);
    });

    private final IAction action;

    ActionEnum(IAction action) {
        this.action = action;
    }

    @Override
    public void execute() {
        action.execute();
    }

    public static String getStringInput(String param) {
        Scanner sc = new Scanner(System.in);
        String str = "";
        System.out.println(param);
        str = sc.nextLine();

        return str;
    }

    private static List<String> getDateInput() {
        String dateAfter;
        do {
            dateAfter = getStringInput("Enter first date (yyyy-mm-dd):");
        } while (!StringUtils.isDate(dateAfter));

        String dateBefore;
        do {
            dateBefore = getStringInput("Enter second date (yyyy-mm-dd):");
        } while (!StringUtils.isDate(dateBefore));

        List<String> dates = new ArrayList<>();
        dates.add(dateAfter);
        dates.add(dateBefore);
        return dates;
    }

    private static int getNumber(String message, int maxNumber) {
        String str;
        int num;

        do {
            str = getStringInput(message);
            while (!StringUtils.isNumeric(str)) {
                System.out.println("That's not a number. Please try again:");
                str = getStringInput(message);
            }
            num = Integer.parseInt(str.trim());
        } while (num < 0 || num > maxNumber);

        return num;
    }
}
