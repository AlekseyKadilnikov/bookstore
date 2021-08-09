package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.utils.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class AdminMenuBuilder implements UserMenuBuilder {
    @Override
    public void createOrderItem() {
        MenuItem item = new MenuItem("Orders", MenuUtils.adminOrderMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    @Override
    public void createCatalogItem() {
        MenuItem item = new MenuItem("Catalog", MenuUtils.adminCatalogMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.rootMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }

    public void createRequestItem() {
        MenuItem item = new MenuItem("Show requests for book", null, ActionEnum.SHOW_REQUESTS_FOR_BOOK);
        MenuUtils.adminMenu.addItem(item);
    }

    public void createImportExportItem() {
        MenuItem item = new MenuItem("Import&Export", MenuUtils.importExportMenu, null);
        MenuUtils.adminMenu.addItem(item);
    }
}
