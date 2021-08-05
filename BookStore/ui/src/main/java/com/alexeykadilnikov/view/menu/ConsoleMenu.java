package com.alexeykadilnikov.view.menu;

import java.util.ArrayList;
import java.util.List;

public class ConsoleMenu {
    private String name;
    private List<MenuItem> menuItems = new ArrayList<>();

    public ConsoleMenu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItem(int index) {
        return menuItems.get(index);
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void addItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }
}
