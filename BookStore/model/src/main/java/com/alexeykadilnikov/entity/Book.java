package com.alexeykadilnikov.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Book extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2032341091985408913L;
    private static long idCount = 0;
    private String name;
    private List<Long> authors;
    private String publisher;
    private int publicationYear;
    private int price;
    private int count;
    private LocalDate dateOfReceipt = LocalDate.now();
    private String description = "";
    private final List<Request> commonRequests = new ArrayList<>();
    private final Request[] orderRequests = new Request[2];

    public Book() {
        super(idCount++);
    }

    public Book(String name, List<Long> authors, String publisher, int publicationYear, int price, int count) {
        super(idCount++);
        this.name = name;
        this.authors = authors;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id = " + getId() +
                ", name='" + name + '\'' +
                ", author='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", dateOfReceipt=" + dateOfReceipt +
                ", dateOfPublication=" + publicationYear +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear && price == book.price && count == book.count
                && name.equals(book.name) && authors.equals(book.authors) && publisher.equals(book.publisher)
                && dateOfReceipt.equals(book.dateOfReceipt) && description.equals(book.description)
                && commonRequests.equals(book.commonRequests) && Arrays.equals(orderRequests, book.orderRequests);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, authors, publisher, publicationYear, price, count, dateOfReceipt, description, commonRequests);
        result = 31 * result + Arrays.hashCode(orderRequests);
        return result;
    }

    public List<Request> getCommonRequests() {
        return commonRequests;
    }

    public Request[] getOrderRequests() {
        return orderRequests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(LocalDate dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Long> authors) {
        this.authors = authors;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
