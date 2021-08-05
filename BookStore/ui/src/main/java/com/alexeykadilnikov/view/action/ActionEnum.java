package com.alexeykadilnikov.view.action;

import com.alexeykadilnikov.BookComparator;
import com.alexeykadilnikov.controller.BookController;
import com.alexeykadilnikov.controller.RequestController;
import com.alexeykadilnikov.controller.UserController;

import java.util.Scanner;

public enum ActionEnum implements IAction {
    CREATE_USER(() -> {
        UserController userController = UserController.getInstance();
        String username = getStringInput("Enter new username:");

        while (userController.create(username) > 0) {
            username = getStringInput("This username already created. Please try again\nEnter new username:");
        }
    }),

    SIGN_IN(() -> {
        UserController userController = UserController.getInstance();
        String username = getStringInput("Enter username:");

        while (userController.getOne(username) > 0) {
            username = getStringInput("User not found. Please try again:\nEnter username:");
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
