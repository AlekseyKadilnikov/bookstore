package com.alexeykadilnikov.view.action;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.controller.BookController;
import com.alexeykadilnikov.controller.OrderController;
import com.alexeykadilnikov.controller.RequestController;
import com.alexeykadilnikov.controller.UserController;
import com.alexeykadilnikov.entity.User;
import com.alexeykadilnikov.service.UserService;
import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.utils.UserUtils;
import com.alexeykadilnikov.view.menu.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.alexeykadilnikov.utils.MenuUtils.rootMenu;

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

    SORT_BY_NAME(() -> {
        BookController bookController = BookController.getInstance();
        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));
        System.out.println(sortType);
        while (sortType != 1 && sortType != 0) {
            sortType = Integer.parseInt(getStringInput("Invalid selection. Please try again\n" +
                    "Enter sort type:\n0. Ascending\n1. Descending"));
        }

        if(sortType == 0) {
            bookController.sort(BookComparator.NameComparatorAscending);
        }
        else {
            bookController.sort(BookComparator.NameComparatorDescending);
        }
    }),

    SORT_BY_PRICE(() -> {
        BookController bookController = BookController.getInstance();
        int sortType = Integer.parseInt(getStringInput("Enter sort type:\n0.Ascending\n1.Descending"));
        while (sortType != 1 && sortType != 0) {
            sortType = Integer.parseInt(getStringInput("Invalid selection. Please try again\n" +
                    "Enter sort type:\n0. Ascending\n1. Descending"));
        }

        if(sortType == 0) {
            bookController.sort(BookComparator.PriceComparatorAscending);
        }
        else {
            bookController.sort(BookComparator.PriceComparatorDescending);
        }
    }),

    NEW_ORDER(() -> {
        BookController bookController = BookController.getInstance();
        bookController.sort(BookComparator.PriceComparatorAscending);

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

    CANCEL_ORDER(() -> {
        OrderController orderController = OrderController.getInstance();
        int orderId = Integer.parseInt(getStringInput("Enter order id:"));
        while (orderId < 0) {
            orderId = Integer.parseInt(getStringInput("Invalid selection. Please try again\n" +
                    "Enter order id:"));
        }
        orderController.cancel(orderId);
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
        String username = "";
        System.out.println(param);
        username = sc.nextLine();

        return username;
    }
}
