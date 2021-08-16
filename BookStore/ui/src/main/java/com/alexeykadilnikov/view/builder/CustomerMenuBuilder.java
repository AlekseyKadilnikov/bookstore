package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.view.menu.MenuItem;

public class CustomerMenuBuilder implements UserMenuBuilder {
    @Override
    public void createOrderItem() {
        MenuItem item = new MenuItem("Orders", MenuUtils.customerOrderMenu, null);
        MenuUtils.customerMenu.addItem(item);
    }

    @Override
    public void createCatalogItem() {
        MenuItem item = new MenuItem("Catalog", MenuUtils.customerCatalogMenu, null);
        MenuUtils.customerMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.rootMenu, null);
        MenuUtils.customerMenu.addItem(item);
    }

    @Override
    public void createExitItem() {
        MenuItem item = new MenuItem("Exit", null, ActionEnum.EXIT);
        MenuUtils.customerMenu.addItem(item);
    }
}
