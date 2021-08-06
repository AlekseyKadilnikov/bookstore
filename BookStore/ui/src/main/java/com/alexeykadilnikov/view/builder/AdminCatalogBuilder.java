package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class AdminCatalogBuilder implements CatalogBuilder{
    @Override
    public void createSearchItem() {
        MenuItem item = new MenuItem("Search", null, ActionEnum.SEARCH);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    @Override
    public void createSortByNameItem() {
        MenuItem item = new MenuItem("Sort by name", null, ActionEnum.SORT_BOOKS_BY_NAME);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    @Override
    public void createSortByPriceItem() {
        MenuItem item = new MenuItem("Sort by price", null, ActionEnum.SORT_BOOKS_BY_PRICE);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    @Override
    public void createShowDescriptionItem() {
        MenuItem item = new MenuItem("Get book description", null, ActionEnum.SHOW_BOOK_DESCRIPTION);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.adminMenu, null);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    public void createStaleBooksItem() {
        MenuItem item = new MenuItem("Show stale books", null, ActionEnum.SHOW_STALE_BOOKS);
        MenuUtils.adminCatalogMenu.addItem(item);
    }
}
