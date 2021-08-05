package com.alexeykadilnikov.view.menu;

import com.alexeykadilnikov.view.builder.Builder;

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

        navigator.printMenu();
    }
}
