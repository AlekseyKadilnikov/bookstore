package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;
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

    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.customerMenu, null);
        MenuUtils.customerCatalogMenu.addItem(item);
    }

    public void createNewOrderItem() {
        MenuItem item = new MenuItem("Create order", null, ActionEnum.NEW_ORDER);
        MenuUtils.customerCatalogMenu.addItem(item);
    }
}
