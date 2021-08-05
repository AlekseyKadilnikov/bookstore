package com.alexeykadilnikov.view.menu;

import java.util.List;

public class Navigator {
    private ConsoleMenu currentMenu;

    public Navigator(ConsoleMenu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public ConsoleMenu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(ConsoleMenu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        printHeader();

        List<MenuItem> menuItems = currentMenu.getMenuItems();
        for(int i = 0; i < menuItems.size(); i++) {
            System.out.println(i + ". " + menuItems.get(i).getTitle());
        }
    }

    public void navigate(int index) {
        MenuItem item = currentMenu.getMenuItems().get(index);
        if(item.getNextMenu() != null) {
            currentMenu = item.getNextMenu();
        }
        else {
            item.doAction();
        }

    }

    private void printHeader() {
        System.out.println("\n******************************************************");
        System.out.println("\t" + currentMenu.getName().toUpperCase());
        System.out.println("******************************************************");
    }
}
