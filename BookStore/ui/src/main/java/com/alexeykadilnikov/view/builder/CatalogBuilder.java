package com.alexeykadilnikov.view.builder;

public interface CatalogBuilder extends BaseBuilder {
    void createSearchItem();
    void createSortByNameItem();
    void createSortByPriceItem();
    void createShowDescriptionItem();
}
