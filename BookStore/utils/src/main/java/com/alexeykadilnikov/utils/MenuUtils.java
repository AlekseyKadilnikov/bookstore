package com.alexeykadilnikov.utils;

import com.alexeykadilnikov.view.menu.Menu;

public class MenuUtils {
    public static Menu mainMenu = new Menu("MAIN MENU");
    public static Menu customerMenu = new Menu("CUSTOMER MENU");
    public static Menu adminMenu = new Menu("ADMIN MENU");
    public static Menu customerOrderMenu = new Menu("CUSTOMER ORDER MENU");
    public static Menu adminOrderMenu = new Menu("ADMIN ORDER MENU");
    public static Menu baseCatalogMenu = new Menu("BASE CATALOG MENU");
    public static Menu customerCatalogMenu = new Menu("CUSTOMER CATALOG MENU");
    public static Menu adminCatalogMenu = new Menu("ADMIN CATALOG MENU");
    public static Menu editBook = new Menu("EDIT BOOK");
    public static Menu addBook = new Menu("ADD BOOK");

    private MenuUtils() {
    }
}
