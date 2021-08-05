package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class CustomerMenuBuilder implements UserMenuBuilder {
    @Override
    public void createOrderItem() {
        MenuItem item = new MenuItem("Orders", MenuUtils.customerOrderMenu, ActionEnum.CREATE_USER);
        MenuUtils.customerMenu.addItem(item);
    }

    @Override
    public void createCatalogItem() {

    }

    @Override
    public void createGoBackItem() {

    }
}
