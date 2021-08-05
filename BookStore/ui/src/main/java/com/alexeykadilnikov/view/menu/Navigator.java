package com.alexeykadilnikov.view.menu;

public class Navigator {
    private Menu currentMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {

    }

    public void navigate(int index) {

    }
}
