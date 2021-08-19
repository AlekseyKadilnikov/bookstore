package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;

import com.alexeykadilnikov.view.menu.ConsoleMenu;

public class Builder {
    private ConsoleMenu rootMenu;

    public void buildMenu() {
        rootMenu = MenuUtils.rootMenu;

        RootMenuBuilder rootMenuBuilder = new RootMenuBuilder();
        rootMenuBuilder.createSignInItem();
        rootMenuBuilder.createRegistrationItem();
        rootMenuBuilder.createBaseCatalogItem();
        rootMenuBuilder.createExitItem();

        BaseCatalogBuilder baseCatalogBuilder = new BaseCatalogBuilder();
        baseCatalogBuilder.createSearchItem();
        baseCatalogBuilder.createShowDescriptionItem();
        baseCatalogBuilder.createSortByNameItem();
        baseCatalogBuilder.createSortByPriceItem();
        baseCatalogBuilder.createCountItem();
        baseCatalogBuilder.createDateItem();
        baseCatalogBuilder.createGoBackItem();
        baseCatalogBuilder.createExitItem();

        CustomerMenuBuilder customerMenuBuilder = new CustomerMenuBuilder();
        customerMenuBuilder.createCatalogItem();
        customerMenuBuilder.createOrderItem();
        customerMenuBuilder.createGoBackItem();
        customerMenuBuilder.createExitItem();

        AdminMenuBuilder adminMenuBuilder = new AdminMenuBuilder();
        adminMenuBuilder.createCatalogItem();
        adminMenuBuilder.createOrderItem();
        adminMenuBuilder.createRequestItem();
        adminMenuBuilder.createImportExportItem();
        adminMenuBuilder.createGoBackItem();
        adminMenuBuilder.createExitItem();

        CustomerOrderMenuBuilder customerOrderMenuBuilder = new CustomerOrderMenuBuilder();
        customerOrderMenuBuilder.createGetOrdersItem();
        customerOrderMenuBuilder.createCancelOrderItem();
        customerOrderMenuBuilder.createGoBackItem();
        customerOrderMenuBuilder.createExitItem();

        AdminOrderMenuBuilder adminOrderMenuBuilder = new AdminOrderMenuBuilder();
        adminOrderMenuBuilder.createGetOrdersItem();
        adminOrderMenuBuilder.createCancelOrderItem();
        adminOrderMenuBuilder.createCompletedOrdersCountItem();
        adminOrderMenuBuilder.createGetOrderDetailItem();
        adminOrderMenuBuilder.createGetEarnedMoneyItem();
        adminOrderMenuBuilder.createSortByExecutionDateItem();
        adminOrderMenuBuilder.createSortByExecutionItem();
        adminOrderMenuBuilder.createSetOrderStatusItem();
        adminOrderMenuBuilder.createSortByPriceItem();
        adminOrderMenuBuilder.createSortByStatusItem();
        adminOrderMenuBuilder.createGoBackItem();
        adminOrderMenuBuilder.createExitItem();

        CustomerCatalogBuilder customerCatalogBuilder = new CustomerCatalogBuilder();
        customerCatalogBuilder.createSearchItem();
        customerCatalogBuilder.createShowDescriptionItem();
        customerCatalogBuilder.createNewOrderItem();
        customerCatalogBuilder.createSortByNameItem();
        customerCatalogBuilder.createSortByPriceItem();
        customerCatalogBuilder.createCountItem();
        customerCatalogBuilder.createDateItem();
        customerCatalogBuilder.createGoBackItem();
        customerCatalogBuilder.createExitItem();

        AdminCatalogBuilder adminCatalogBuilder = new AdminCatalogBuilder();
        adminCatalogBuilder.createSearchItem();
        adminCatalogBuilder.createShowDescriptionItem();
        adminCatalogBuilder.createSortByNameItem();
        adminCatalogBuilder.createSortByPriceItem();
        adminCatalogBuilder.createStaleBooksItem();
        adminCatalogBuilder.createWriteOffBookItem();
        adminCatalogBuilder.createCountItem();
        adminCatalogBuilder.createAddBookItem();
        adminCatalogBuilder.createDateItem();
        adminCatalogBuilder.createGoBackItem();
        adminCatalogBuilder.createExitItem();

        ImportExportBuilder importExportBuilder = new ImportExportBuilder();
        importExportBuilder.createExportBookItem();
        importExportBuilder.createExportOrderItem();
        importExportBuilder.createExportRequestItem();
        importExportBuilder.createExportUserItem();
        importExportBuilder.createImportBookItem();
        importExportBuilder.createImportOrderItem();
        importExportBuilder.createImportRequestItem();
        importExportBuilder.createImportUserItem();
        importExportBuilder.createGoBackItem();
        importExportBuilder.createExitItem();
    }

    public ConsoleMenu getRootMenu() {
        return rootMenu;
    }
}
