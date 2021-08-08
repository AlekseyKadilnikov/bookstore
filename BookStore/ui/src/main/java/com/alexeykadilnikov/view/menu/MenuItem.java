package com.alexeykadilnikov.view.menu;

import com.alexeykadilnikov.view.action.IAction;

public class MenuItem {
    private String title;
    private ConsoleMenu nextMenu;
    private IAction action;

    public MenuItem() {
    }

    public MenuItem(String title, ConsoleMenu nextMenu, IAction action) {
        this.title = title;
        this.nextMenu = nextMenu;
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ConsoleMenu getNextMenu() {
        return nextMenu;
    }

    public void setNextMenu(ConsoleMenu nextMenu) {
        this.nextMenu = nextMenu;
    }

    public IAction getAction() {
        return action;
    }

    public void setAction(IAction action) {
        this.action = action;
    }

    public void doAction() {
        action.execute();
    }
}
