package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.view.menu.ConsoleMenu;

public class MenuUtils {
    public static ConsoleMenu rootMenu = new ConsoleMenu("ROOT MENU");
    public static ConsoleMenu customerMenu = new ConsoleMenu("CUSTOMER MENU");
    public static ConsoleMenu adminMenu = new ConsoleMenu("ADMIN MENU");
    public static ConsoleMenu customerOrderMenu = new ConsoleMenu("CUSTOMER ORDER MENU");
    public static ConsoleMenu adminOrderMenu = new ConsoleMenu("ADMIN ORDER MENU");
    public static ConsoleMenu baseCatalogMenu = new ConsoleMenu("BASE CATALOG MENU");
    public static ConsoleMenu customerCatalogMenu = new ConsoleMenu("CUSTOMER CATALOG MENU");
    public static ConsoleMenu adminCatalogMenu = new ConsoleMenu("ADMIN CATALOG MENU");
    public static ConsoleMenu adminUserMenu = new ConsoleMenu("USER MENU");
    public static ConsoleMenu adminRequestMenu = new ConsoleMenu("REQUEST MENU");
    public static ConsoleMenu editBook = new ConsoleMenu("EDIT BOOK");
    public static ConsoleMenu addBook = new ConsoleMenu("ADD BOOK");

    private MenuUtils() {
    }
}
