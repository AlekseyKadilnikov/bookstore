package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;

import com.alexeykadilnikov.view.menu.ConsoleMenu;

public class Builder {
    private ConsoleMenu rootMenu;

    public void buildMenu() {
        rootMenu = MenuUtils.rootMenu;

        RootMenuBuilder rootMenuBuilder = new RootMenuBuilder();
        rootMenuBuilder.createSignInItem();
        rootMenuBuilder.createRegistrationItem();
        rootMenuBuilder.createBaseCatalogItem();

        BaseCatalogBuilder baseCatalogBuilder = new BaseCatalogBuilder();
        baseCatalogBuilder.createSearchItem();
        baseCatalogBuilder.createShowDescriptionItem();
        baseCatalogBuilder.createSortByNameItem();
        baseCatalogBuilder.createSortByPriceItem();
        baseCatalogBuilder.createGoBackItem();

        CustomerMenuBuilder customerMenuBuilder = new CustomerMenuBuilder();
        customerMenuBuilder.createCatalogItem();
        customerMenuBuilder.createOrderItem();
        customerMenuBuilder.createGoBackItem();

        AdminMenuBuilder adminMenuBuilder = new AdminMenuBuilder();
        adminMenuBuilder.createCatalogItem();
        adminMenuBuilder.createOrderItem();
        adminMenuBuilder.createRequestItem();
        adminMenuBuilder.createGoBackItem();

        CustomerOrderMenuBuilder customerOrderMenuBuilder = new CustomerOrderMenuBuilder();
        customerOrderMenuBuilder.createGetOrdersItem();
        customerOrderMenuBuilder.createCancelOrderItem();
        customerOrderMenuBuilder.createGoBackItem();

        AdminOrderMenuBuilder adminOrderMenuBuilder = new AdminOrderMenuBuilder();
        adminOrderMenuBuilder.createGetOrdersItem();
        adminOrderMenuBuilder.createCancelOrderItem();
        adminOrderMenuBuilder.createCompletedOrdersCountItem();
        adminOrderMenuBuilder.createGetOrderDetailItem();
        adminOrderMenuBuilder.createGetEarnedMoneyItem();
        adminOrderMenuBuilder.createSortByExecutionDateItem();
        adminOrderMenuBuilder.createSortByPriceItem();
        adminOrderMenuBuilder.createSortByStatusItem();
        adminOrderMenuBuilder.createGoBackItem();

        CustomerCatalogBuilder customerCatalogBuilder = new CustomerCatalogBuilder();
        customerCatalogBuilder.createSearchItem();
        customerCatalogBuilder.createShowDescriptionItem();
        customerCatalogBuilder.createNewOrderItem();
        customerCatalogBuilder.createSortByNameItem();
        customerCatalogBuilder.createSortByPriceItem();
        customerCatalogBuilder.createGoBackItem();

        AdminCatalogBuilder adminCatalogBuilder = new AdminCatalogBuilder();
        adminCatalogBuilder.createSearchItem();
        adminCatalogBuilder.createShowDescriptionItem();
        adminCatalogBuilder.createSortByNameItem();
        adminCatalogBuilder.createSortByPriceItem();
        adminCatalogBuilder.createStaleBooksItem();
        adminCatalogBuilder.createGoBackItem();
    }

    public ConsoleMenu getRootMenu() {
        return rootMenu;
    }
}
