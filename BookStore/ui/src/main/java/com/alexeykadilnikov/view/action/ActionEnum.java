package com.alexeykadilnikov.view.action;

import com.alexeykadilnikov.OrderStatus;
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
        String username = getStringInput("Enter new username:");

        while (ControllerUtils.userController.create(username) > 0) {
            username = getStringInput("This username already created. Please try again\nEnter new username:");
        }

        ControllerUtils.userController.create(username);

        UserUtils.setCurrentUser(ControllerUtils.userController.getOne(username));
    }),

    SIGN_IN(() -> {
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
            default:
                break;
        }
    }),

    SORT_BOOKS_BY_NAME(() -> {
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sortByName(0);
        }
        else {
            ControllerUtils.bookController.sortByName(1);
        }
    }),

    SORT_BOOKS_BY_PRICE(() -> {
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sortByPrice(0);
        }
        else {
            ControllerUtils.bookController.sortByPrice(1);
        }
    }),

    WRITE_OFF_BOOK(() -> {
        long bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        ControllerUtils.bookController.writeOff(bookId);
    }),

    ADD_BOOK(() -> {
        long bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        int count = getNumber("Enter count of books:", Integer.MAX_VALUE);
        ControllerUtils.bookController.addBook(bookId, count);
    }),

    SORT_ORDERS_BY_PRICE(() -> {
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending:", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.orderController.sortByPrice(0);
        }
        else {
            ControllerUtils.orderController.sortByPrice(1);
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
            default:
                break;
        }
    }),

    SORT_ORDERS_BY_EXEC_DATE(() -> {
        List<String> dates = getDateInput();

        int sortNameNum = getNumber("Enter sort type:\n0.By date\n1.By price", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortNameNum == 0 && sortTypeNum == 0) {
            ControllerUtils.orderController.sortByExecDateForPeriodByDate(dates.get(0), dates.get(1), 0);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            ControllerUtils.orderController.sortByExecDateForPeriodByDate(dates.get(0), dates.get(1), 1);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            ControllerUtils.orderController.sortByExecDateForPeriodByPrice(dates.get(0), dates.get(1), 0);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            ControllerUtils.orderController.sortByExecDateForPeriodByPrice(dates.get(0), dates.get(1), 1);
        }
    }),

    SORT_BY_EXEC_DATE(() -> {
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.orderController.sortByExecDate(0);
        }
        else {
            ControllerUtils.orderController.sortByExecDate(1);
        }
    }),

    EARNED_MONEY(() -> {
        List<String> dates = getDateInput();
        ControllerUtils.orderController.getEarnedMoneyForPeriod(dates.get(0), dates.get(1));
    }),

    COMPL_ORDERS_COUNT(() -> {
        List<String> dates = getDateInput();
        ControllerUtils.orderController.getCountOfCompleteOrdersForPeriod(dates.get(0), dates.get(1));
    }),

    GET_ORDER_DETAIL(() -> {
        int orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        ControllerUtils.orderController.showOne(orderId);
    }),

    SORT_BY_COUNT(() -> {
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sortByCount(0);
        }
        else {
            ControllerUtils.bookController.sortByCount(1);
        }
    }),

    SORT_BOOKS_BY_DATE(() -> {
        List<Book> books = ControllerUtils.bookController.getAll();
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortTypeNum == 0) {
            ControllerUtils.bookController.sortByPublicationYear(0);
        }
        else {
            ControllerUtils.bookController.sortByPublicationYear(1);
        }
    }),

    NEW_ORDER(() -> {
        String bookIds = getStringInput("Enter book ids (example: 1 1 5)");
        String[] stringIds = bookIds.split(" ");
        List<Long> longIds = new ArrayList<>();
        for(String id : stringIds) {
            longIds.add(Long.parseLong(id));
        }

        ControllerUtils.orderController.create(longIds);
    }),

    GET_USER_ORDERS(() -> {
        ControllerUtils.userController.getOrders(UserUtils.getCurrentUser());
    }),

    GET_ADMIN_ORDERS(ControllerUtils.orderController::showAll),

    CANCEL_ORDER(() -> {
        long orderId = getNumber("Enter order id:", Integer.MAX_VALUE);
        ControllerUtils.orderController.cancel(orderId);
    }),

    SHOW_BOOK_DESCRIPTION(() -> {
        long bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        ControllerUtils.bookController.showDescription(bookId);
    }),

    SHOW_REQUESTS_FOR_BOOK(() -> {
        int bookId = getNumber("Enter book id:", Integer.MAX_VALUE);
        int sortNameNum = getNumber("Enter number of sort type:\n0.By books count\n1.By name", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);

        if(sortNameNum == 0 && sortTypeNum == 0) {
            ControllerUtils.requestController.getRequestsForBookSortedByCount(bookId, 0);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            ControllerUtils.requestController.getRequestsForBookSortedByCount(bookId, 1);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            ControllerUtils.requestController.getRequestsForBookSortedByName(bookId, 0);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            ControllerUtils.requestController.getRequestsForBookSortedByName(bookId, 1);
        }
    }),

    SHOW_STALE_BOOKS(() -> {
        int sortNameNum = getNumber("Enter sort type:\n0.By date of receipt\n1.By price", 1);
        int sortTypeNum = getNumber("Enter sort type:\n0.Ascending\n1.Descending", 1);
        if(sortNameNum == 0 && sortTypeNum == 0) {
            ControllerUtils.bookController.getStaleBooksByDate(0);
        }
        else if(sortNameNum == 0 && sortTypeNum == 1){
            ControllerUtils.bookController.getStaleBooksByDate(1);
        }
        else if(sortNameNum == 1 && sortTypeNum == 0) {
            ControllerUtils.bookController.getStaleBooksByPrice(0);
        }
        else if(sortNameNum == 1 && sortTypeNum == 1) {
            ControllerUtils.bookController.getStaleBooksByPrice(1);
        }
    }),

    IMPORT_USERS(() -> {
        String path = getStringInput("Enter .csv file path:");
        ControllerUtils.userController.importUsers(path);
    }),

    EXPORT_USERS(() -> {
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
