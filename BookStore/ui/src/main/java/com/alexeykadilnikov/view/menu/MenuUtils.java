package com.alexeykadilnikov.view.menu;

public class MenuUtils {
    public static final ConsoleMenu rootMenu = new ConsoleMenu("ROOT MENU");
    public static final ConsoleMenu customerMenu = new ConsoleMenu("CUSTOMER MENU");
    public static final ConsoleMenu adminMenu = new ConsoleMenu("ADMIN MENU");
    public static final ConsoleMenu customerOrderMenu = new ConsoleMenu("CUSTOMER ORDER MENU");
    public static final ConsoleMenu adminOrderMenu = new ConsoleMenu("ADMIN ORDER MENU");
    public static final ConsoleMenu baseCatalogMenu = new ConsoleMenu("BASE CATALOG MENU");
    public static final ConsoleMenu customerCatalogMenu = new ConsoleMenu("CUSTOMER CATALOG MENU");
    public static final ConsoleMenu adminCatalogMenu = new ConsoleMenu("ADMIN CATALOG MENU");
    public static final ConsoleMenu importExportMenu = new ConsoleMenu("IMPORT & EXPORT");

    private MenuUtils() {
    }
}
