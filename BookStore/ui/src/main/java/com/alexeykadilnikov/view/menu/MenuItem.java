package com.alexeykadilnikov.view.menu;

import com.alexeykadilnikov.view.action.IAction;

import java.awt.Menu;


public class MenuItem {
    private String title;
    private java.awt.Menu nextMenu;
    private IAction action;

    public MenuItem() {
    }

    public MenuItem(String title, Menu nextMenu, IAction action) {
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

    public java.awt.Menu getNextMenu() {
        return nextMenu;
    }

    public void setNextMenu(Menu nextMenu) {
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
