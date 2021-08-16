package com.alexeykadilnikov.view.builder;

import com.alexeykadilnikov.view.menu.MenuUtils;
import com.alexeykadilnikov.view.action.ActionEnum;
import com.alexeykadilnikov.view.menu.MenuItem;

public class RootMenuBuilder {

    public void createSignInItem() {
        MenuItem item = new MenuItem("Sign in", null, ActionEnum.SIGN_IN);
        MenuUtils.rootMenu.addItem(item);
    }

    public void createRegistrationItem() {
        MenuItem item = new MenuItem("Create account", MenuUtils.customerMenu, ActionEnum.CREATE_USER);
        MenuUtils.rootMenu.addItem(item);
    }

    public void createBaseCatalogItem() {
        MenuItem item = new MenuItem("Catalog", MenuUtils.baseCatalogMenu, null);
        MenuUtils.rootMenu.addItem(item);
    }
}
