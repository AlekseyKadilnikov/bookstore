package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;
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
        MenuItem item = new MenuItem("Sort by execution date for period", null, ActionEnum.SORT_ORDERS_BY_EXEC_DATE);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createGetEarnedMoneyItem() {
        MenuItem item = new MenuItem("Get earned money for period", null, ActionEnum.EARNED_MONEY);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createCompletedOrdersCountItem() {
        MenuItem item = new MenuItem("Get count of completed orders for period", null, ActionEnum.COMPL_ORDERS_COUNT);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createGetOrderDetailItem() {
        MenuItem item = new MenuItem("Get order detail", null, ActionEnum.GET_ORDER_DETAIL);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createSetOrderStatusItem() {
        MenuItem item = new MenuItem("Set order status", null, ActionEnum.SET_ORDER_STATUS);
        MenuUtils.adminOrderMenu.addItem(item);
    }

    public void createSortByExecutionItem() {
        MenuItem item = new MenuItem("Sort by execution date", null, ActionEnum.SORT_BY_EXEC_DATE);
        MenuUtils.adminOrderMenu.addItem(item);
    }
}
