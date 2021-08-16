package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class CustomerOrderMenuBuilder implements OrderMenuBuilder {
    @Override
    public void createGetOrdersItem() {
        MenuItem item = new MenuItem("My orders", null, ActionEnum.GET_USER_ORDERS);
        MenuUtils.customerOrderMenu.addItem(item);
    }

    @Override
    public void createCancelOrderItem() {
        MenuItem item = new MenuItem("Cancel order", null, ActionEnum.CANCEL_ORDER);
        MenuUtils.customerOrderMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.customerMenu, null);
        MenuUtils.customerOrderMenu.addItem(item);
    }

    @Override
    public void createExitItem() {
        MenuItem item = new MenuItem("Exit", null, ActionEnum.EXIT);
        MenuUtils.customerOrderMenu.addItem(item);
    }
}
