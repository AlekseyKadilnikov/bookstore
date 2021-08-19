package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class CustomerCatalogBuilder implements CatalogBuilder {
    @Override
    public void createSearchItem() {
        MenuItem item = new MenuItem("Search", null, ActionEnum.SEARCH);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createSortByNameItem() {
        MenuItem item = new MenuItem("Sort by name", null, ActionEnum.SORT_BOOKS_BY_NAME);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createSortByPriceItem() {
        MenuItem item = new MenuItem("Sort by price", null, ActionEnum.SORT_BOOKS_BY_PRICE);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createShowDescriptionItem() {
        MenuItem item = new MenuItem("Get book description", null, ActionEnum.SHOW_BOOK_DESCRIPTION);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.customerMenu, null);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createCountItem() {
        MenuItem item = new MenuItem("Sort by count in storage", null, ActionEnum.SORT_BY_COUNT);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createDateItem() {
        MenuItem item = new MenuItem("Sort by date of publication", null, ActionEnum.SORT_BOOKS_BY_DATE);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    @Override
    public void createExitItem() {
        MenuItem item = new MenuItem("Exit", null, ActionEnum.EXIT);
        MenuUtils.customerCatalogMenu.addItem(item);
    }
    public void createNewOrderItem() {
        MenuItem item = new MenuItem("Create order", null, ActionEnum.NEW_ORDER);
        MenuUtils.customerCatalogMenu.addItem(item);
    }
}
