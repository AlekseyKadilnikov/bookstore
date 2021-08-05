package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.view.menu.MenuItem;

public class AdminMenuBuilder implements UserMenuBuilder {
    @Override
    public void createOrderItem() {
        MenuItem item = new MenuItem("Orders", MenuUtils.adminOrderMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    @Override
    public void createCatalogItem() {
        MenuItem item = new MenuItem("Catalog", MenuUtils.adminCatalogMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.rootMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    public void createUserItem() {
        MenuItem item = new MenuItem("Users", MenuUtils.adminUserMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    public void createRequestItem() {
        MenuItem item = new MenuItem("Requests", MenuUtils.adminRequestMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }
}
