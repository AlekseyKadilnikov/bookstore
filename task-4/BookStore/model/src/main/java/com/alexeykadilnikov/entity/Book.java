package com.alexeykadilnikov.entity;

import java.util.Comparator;

public class Book extends BaseEntity {
    private static long ID_COUNT = 0;
    private String name;
    private String author;
    private String publisher;
    private int publicationYear;
    private int price;
    private boolean available;

    public Book(String name, String author, String publisher, int publicationYear, int price, boolean available) {
        super(ID_COUNT++);
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.price = price;
        this.available = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", available=" + available +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
