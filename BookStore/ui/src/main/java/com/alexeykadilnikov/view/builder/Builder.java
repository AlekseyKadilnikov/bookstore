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
        baseCatalogBuilder.createSortByNameItem();
        baseCatalogBuilder.createGoBackItem();
    }

    public ConsoleMenu getRootMenu() {
        return rootMenu;
    }
}
