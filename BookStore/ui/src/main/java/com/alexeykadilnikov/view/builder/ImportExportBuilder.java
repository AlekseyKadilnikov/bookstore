package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class ImportExportBuilder implements BaseBuilder {
    @Override
    public void createGoBackItem() {
        MenuItem item = new MenuItem("Back", MenuUtils.adminMenu, null);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createImportBookItem() {
        MenuItem item = new MenuItem("Import books", null, ActionEnum.IMPORT_BOOKS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createImportOrderItem() {
        MenuItem item = new MenuItem("Import orders", null, ActionEnum.IMPORT_ORDERS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createImportRequestItem() {
        MenuItem item = new MenuItem("Import requests", null, ActionEnum.IMPORT_REQUESTS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createImportUserItem() {
        MenuItem item = new MenuItem("Import users", null, ActionEnum.IMPORT_USERS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createExportBookItem() {
        MenuItem item = new MenuItem("Export books", null, ActionEnum.EXPORT_BOOKS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createExportOrderItem() {
        MenuItem item = new MenuItem("Export orders", null, ActionEnum.EXPORT_ORDERS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createExportRequestItem() {
        MenuItem item = new MenuItem("Export requests", null, ActionEnum.EXPORT_REQUESTS);
        MenuUtils.importExportMenu.addItem(item);
    }

    public void createExportUserItem() {
        MenuItem item = new MenuItem("Export users", null, ActionEnum.EXPORT_USERS);
        MenuUtils.importExportMenu.addItem(item);
    }
}
