package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class AdminOrderMenuBuilder implements OrderMenuBuilder {
    @Override
    public void createGetOrdersItem() {
        MenuItem item = new MenuItem("Get orders", null, ActionEnum.GET_ADMIN_ORDERS);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    @Override
    public void createCancelOrderItem() {
        MenuItem item = new MenuItem("Cancel order", null, ActionEnum.CANCEL_ORDER);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.adminMenu, null);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createSortByPriceItem() {
        MenuItem item = new MenuItem("Sort by price", null, ActionEnum.SORT_ORDERS_BY_PRICE);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createSortByStatusItem() {
        MenuItem item = new MenuItem("Sort by status", null, ActionEnum.SORT_ORDERS_BY_STATUS);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createSortByExecutionDateItem() {
        MenuItem item = new MenuItem("Sort by execution date", null, ActionEnum.SORT_ORDERS_BY_EXEC_DATE);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createGetEarnedMoneyItem() {

    }

    public void createGetCompletedOrdersItem() {

    }

    public void createGetOrderDetailItem() {

    }
}
