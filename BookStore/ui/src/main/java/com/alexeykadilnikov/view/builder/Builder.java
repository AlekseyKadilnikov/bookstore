package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;

import com.alexeykadilnikov.view.menu.Menu;

public class Builder {
    private Menu rootMenu;

    public void buildMenu() {
        rootMenu = MenuUtils.mainMenu;


    }

    public Menu getRootMenu() {
        return rootMenu;
    }
}
