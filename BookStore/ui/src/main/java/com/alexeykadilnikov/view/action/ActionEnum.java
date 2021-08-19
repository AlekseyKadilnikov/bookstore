package com.alexeykadilnikov.view.action;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.OrderComparator;
import com.alexeykadilnikov.OrderStatus;
import com.alexeykadilnikov.RequestComparator;
import com.alexeykadilnikov.controller.*;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.utils.StringUtils;
import com.alexeykadilnikov.utils.UserUtils;
import com.alexeykadilnikov.view.menu.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public enum ActionEnum implements IAction {
    CREATE_USER(() -> {
//        UserController userController = UserController.getInstance();
        String username = getStringInput("Enter new username:");

        while (ControllerUtils.userController.create(username) > 0) {
            username = getStringInput("This username already created. Please try again\nEnter new username:");
        }

        ControllerUtils.userController.create(username);

        UserUtils.setCurrentUser(ControllerUtils.userController.getOne(username));
    }),

    SIGN_IN(() -> {
//        UserController userController = UserController.getInstance();
        String username = getStringInput("Enter username:");

        while (ControllerUtils.userController.getOne(username) == null) {
            username = getStringInput("User not found. Please try again:\nEnter username:");
        }

        UserUtils.setCurrentUser(ControllerUtils.userController.getOne(username));

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
        String request = getStringInput("Enter request");

        ControllerUtils.requestController.search(request);
    }),

    SET_ORDER_STATUS(() -> {
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        int statusTypeNum = getNumber("Enter status number:\n0.New\n1.Success\n2.Canceled", 2);
        switch (statusTypeNum) {
            case 0:
                ControllerUtils.orderController.setStatus(orderId, OrderStatus.NEW);
                break;
            case 1:
                ControllerUtils.orderController.setStatus(orderId, OrderStatus.SUCCESS);
                break;
            case 2:
                ControllerUtils.orderController.setStatus(orderId, OrderStatus.CANCELED);
                break;
        }
    }),

    SORT_BOOKS_BY_NAME(() -> {
        List<Book> books = ControllerUtils.bookController.getAll();

        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sort(books, BookComparator.NameComparatorAscending);
        }
        else {
            ControllerUtils.bookController.sort(books, BookComparator.NameComparatorDescending);
        }
    }),

    SORT_BOOKS_BY_PRICE(() -> {
        List<Book> books = ControllerUtils.bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sort(books, BookComparator.PriceComparatorAscending);
        }
        else {
            ControllerUtils.bookController.sort(books, BookComparator.PriceComparatorDescending);
        }
    }),

    WRITE_OFF_BOOK(() -> {
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        ControllerUtils.bookController.writeOff(bookId);
    }),

    ADD_BOOK(() -> {
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        int count = getNumber("Enter count of books:", Integer.MAX_VALUE);
        ControllerUtils.bookController.addBook(bookId, count);
    }),

    SORT_ORDERS_BY_PRICE(() -> {
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending:", 1);
        List<Order> orders = ControllerUtils.orderController.getAll();
        if(sortTypeNum == 0) {
            ControllerUtils.orderController.sort(orders, OrderComparator.PriceComparatorAscending);
        }
        else {
            ControllerUtils.orderController.sort(orders, OrderComparator.PriceComparatorDescending);
        }
    }),

    SORT_ORDERS_BY_STATUS(() -> {
        int sortTypeNum = getNumber("Enter status number:\n0.New\n1.Success\n2.Canceled", 2);

        switch (sortTypeNum) {
            case 0:
                System.out.println(ControllerUtils.orderController.sortByStatus(OrderStatus.NEW).toString());
                break;
            case 1:
                System.out.println(ControllerUtils.orderController.sortByStatus(OrderStatus.SUCCESS).toString());
                break;
            case 2:
                System.out.println(ControllerUtils.orderController.sortByStatus(OrderStatus.CANCELED).toString());
                break;
        }
    }),

    SORT_ORDERS_BY_EXEC_DATE(() -> {
        List<String> dates = getDateInput();

        int sortNameNum = getNumber("Enter sort type:\n0.By date\n1.By price", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortNameNum == 0 && sortTypeNum == 0) {
            ControllerUtils.orderController.getCompletedOrdersForPeriod(OrderComparator.DateComparatorAscending, dates.get(0), dates.get(1));
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            ControllerUtils.orderController.getCompletedOrdersForPeriod(OrderComparator.DateComparatorDescending, dates.get(0), dates.get(1));
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            ControllerUtils.orderController.getCompletedOrdersForPeriod(OrderComparator.PriceComparatorAscending, dates.get(0), dates.get(1));
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            ControllerUtils.orderController.getCompletedOrdersForPeriod(OrderComparator.PriceComparatorDescending, dates.get(0), dates.get(1));
        }
    }),

    SORT_BY_EXEC_DATE(() -> {
        List<Order> orders = ControllerUtils.orderController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.orderController.sort(orders, OrderComparator.DateComparatorAscending);
        }
        else {
            ControllerUtils.orderController.sort(orders, OrderComparator.DateComparatorDescending);
        }
    }),

    EARNED_MONEY(() -> {
        List<String> dates = getDateInput();
        ControllerUtils.orderController.showEarnedMoneyForPeriod(dates.get(0), dates.get(1));
    }),

    COMPL_ORDERS_COUNT(() -> {
        List<String> dates = getDateInput();
        ControllerUtils.orderController.showCompletedOrdersCountForPeriod(dates.get(0), dates.get(1));
    }),

    GET_ORDER_DETAIL(() -> {
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        ControllerUtils.orderController.showOne(orderId);
    }),

    SORT_BY_COUNT(() -> {
        List<Book> books = ControllerUtils.bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sort(books, BookComparator.AvailableComparatorAscending);
        }
        else {
            ControllerUtils.bookController.sort(books, BookComparator.AvailableComparatorDescending);
        }
    }),

    SORT_BOOKS_BY_DATE(() -> {
        List<Book> books = ControllerUtils.bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sort(books, BookComparator.DateComparatorAscending);
        }
        else {
            ControllerUtils.bookController.sort(books, BookComparator.DateComparatorDescending);
        }
    }),

    NEW_ORDER(() -> {
        List<Book> books = ControllerUtils.bookController.getAll();
        ControllerUtils.bookController.sort(books, BookComparator.PriceComparatorAscending);

        String bookIds = getStringInput("Enter book ids (example: 1 1 5)");
        String[] stringIds = bookIds.split(" ");
        List<Integer> intIds = new ArrayList<>();
        for(String id : stringIds) {
            intIds.add(Integer.parseInt(id));
        }

        ControllerUtils.orderController.create(intIds);
    }),

    GET_USER_ORDERS(() -> {
        ControllerUtils.userController.getOrders(UserUtils.getCurrentUser());
    }),

    GET_ADMIN_ORDERS(ControllerUtils.orderController::showAll),

    CANCEL_ORDER(() -> {
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        ControllerUtils.orderController.cancel(orderId);
    }),

    SHOW_BOOK_DESCRIPTION(() -> {
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        ControllerUtils.bookController.showDescription(bookId);
    }),

    SHOW_REQUESTS_FOR_BOOK(() -> {
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        int sortNameNum = getNumber("Enter number of sort type:\n0.By books count\n1.By name", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortNameNum == 0 && sortTypeNum == 0) {
            ControllerUtils.requestController.sort(bookId, RequestComparator.AmountComparatorAscending);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            ControllerUtils.requestController.sort(bookId, RequestComparator.AmountComparatorDescending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            ControllerUtils.requestController.sort(bookId, RequestComparator.NameComparatorAscending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            ControllerUtils.requestController.sort(bookId, RequestComparator.NameComparatorDescending);
        }
    }),

    SHOW_STALE_BOOKS(() -> {
        FileInputStream fis;
        Properties property = new Properties();
        int months = 6;
        try {
            fis = new FileInputStream("properties\\bookstore.yml");
            property.load(fis);
            months = Integer.parseInt(property.getProperty("months").trim());
            fis.close();
        } catch (IOException e) {
            getLogger().error("Error: file bookstore.yml not found! Months = 6 as default");
        } catch (NumberFormatException e) {
            getLogger().error("Error: invalid parameter format! Months = 6 as default");
        }
        List<Book> staleBooks = ControllerUtils.bookController.getStaleBooks(months);
        int sortNameNum = getNumber("Enter sort type:\n0.By date of receipt\n1.By price", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortNameNum == 0 && sortTypeNum == 0) {
            ControllerUtils.bookController.sort(staleBooks, BookComparator.ReceiptComparatorAscending);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            ControllerUtils.bookController.sort(staleBooks, BookComparator.ReceiptComparatorDescending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            ControllerUtils.bookController.sort(staleBooks, BookComparator.PriceComparatorAscending);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            ControllerUtils.bookController.sort(staleBooks, BookComparator.PriceComparatorDescending);
        }
    }),

    IMPORT_USERS(() -> {
//        UserController userController = UserController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        ControllerUtils.userController.importUsers(path);
    }),

    EXPORT_USERS(() -> {
//        UserController userController = UserController.getInstance();
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter user id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        ControllerUtils.userController.exportUsers(path, ids);
    }),

    IMPORT_BOOKS(() -> {
        String path = getStringInput("Enter .csv file path:");
        ControllerUtils.bookController.importBooks(path);
    }),

    EXPORT_BOOKS(() -> {
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter book id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        ControllerUtils.bookController.exportBooks(path, ids);
    }),

    IMPORT_ORDERS(() -> {
        String path = getStringInput("Enter .csv file path:");
        ControllerUtils.orderController.importOrders(path);
    }),

    EXPORT_ORDERS(() -> {
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter order id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        ControllerUtils.orderController.exportOrders(path, ids);
    }),

    IMPORT_REQUESTS(() -> {
        String path = getStringInput("Enter .csv file path:");
        ControllerUtils.requestController.importRequests(path);
    }),

    EXPORT_REQUESTS(() -> {
        String path = getStringInput("Enter .csv file path:");
        String ids = getStringInput("Enter book id. Example: \"-1\" (all), \"1\" (one), \"1 2 3\" (some)");
        ControllerUtils.requestController.exportRequests(path, ids);
    }),

    EXIT(() -> {
        List<Book> bookList = ControllerUtils.bookController.getAll();
        List<Order> orderList = ControllerUtils.orderController.getAll();
        List<User> userList = ControllerUtils.userController.getAll();
        List<Request> requestList = ControllerUtils.requestController.getAll();

        try {
            FileOutputStream fos = new FileOutputStream("serialize\\books.bin");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(bookList);
            fos = new FileOutputStream("serialize\\orders.bin");
            out = new ObjectOutputStream(fos);
            out.writeObject(orderList);
            fos = new FileOutputStream("serialize\\requests.bin");
            out = new ObjectOutputStream(fos);
            out.writeObject(requestList);
            fos = new FileOutputStream("serialize\\users.bin");
            out = new ObjectOutputStream(fos);
            out.writeObject(userList);
            fos.close();
            out.close();
        }
        catch (IOException e) {
            getLogger().error("Error with writing to file!");
        }

        System.exit(0);
    });

    private static final Logger logger = LoggerFactory.getLogger(ActionEnum.class);

    private final IAction action;

    ActionEnum(IAction action) {
        this.action = action;
    }

    @Override
    public void execute() {
        action.execute();
    }

    public static Logger getLogger() {
        return logger;
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
