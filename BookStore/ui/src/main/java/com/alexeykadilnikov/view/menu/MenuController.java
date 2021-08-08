package com.alexeykadilnikov.view.menu;

import com.alexeykadilnikov.view.builder.Builder;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuController {
    private static MenuController instance;

    private Builder builder;
    private Navigator navigator;

    private MenuController() {
    }

    public static MenuController getInstance() {
        if(instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void run() {
        builder = new Builder();
        builder.buildMenu();
        navigator = new Navigator(builder.getRootMenu());

        int index = 0;
        while (index != -1) {
            navigator.printMenu();
            index = getInput(navigator.getCurrentMenu().getMenuItems().size() - 1);
            navigator.navigate(index);
        }
    }

    private static int getInput(int maxChoice) {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("Enter a number of item:");
            while (!sc.hasNextInt()) {
                System.out.println("That's not a number. Please try again:");
                sc.next();
            }
            choice = sc.nextInt();
        } while (choice < 0 || choice > maxChoice);

        return choice;
    }
}
