package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;
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

    @Override
    public void createCountItem() {
        MenuItem item = new MenuItem("Sort by count in storage", null, ActionEnum.SORT_BY_COUNT);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    @Override
    public void createDateItem() {
        MenuItem item = new MenuItem("Sort by date of publication", null, ActionEnum.SORT_BOOKS_BY_DATE);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    public void createStaleBooksItem() {
        MenuItem item = new MenuItem("Show stale books", null, ActionEnum.SHOW_STALE_BOOKS);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    public void createWriteOffBookItem() {
        MenuItem item = new MenuItem("Write off a book from the warehouse", null, ActionEnum.WRITE_OFF_BOOK);
        MenuUtils.adminCatalogMenu.addItem(item);
    }

    public void createAddBookItem() {
        MenuItem item = new MenuItem("Add book", null, ActionEnum.ADD_BOOK);
        MenuUtils.adminCatalogMenu.addItem(item);
    }
}
