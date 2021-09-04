package com.alexeykadilnikov.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "book")
public class Book extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2032341091985408913L;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Author> authors;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "year")
    private int publicationYear;
    @Column(name = "price")
    private int price;
    @Column(name = "count")
    private int count;
    @Column(name = "date_of_receipt")
    private LocalDate dateOfReceipt;
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "books", fetch = FetchType.EAGER)
    private Set<Request> requests;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<OrderBook> orderBooks;

    public Book() {
    }

    public Book(String name, Set<Author> authors, String publisher, int publicationYear, int price, int count) {
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
                "name='" + name + '\'' +
                ", authors=" + authors +
                ", publisher='" + publisher + '\'' +
                ", publicationYear=" + publicationYear +
                ", price=" + price +
                ", count=" + count +
                ", dateOfReceipt=" + dateOfReceipt +
                ", description='" + description + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return publicationYear == book.publicationYear && price == book.price &&
                count == book.count && name.equals(book.name) &&
                publisher.equals(book.publisher) && dateOfReceipt.equals(book.dateOfReceipt) &&
                description.equals(book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publisher, publicationYear, price,
                count, dateOfReceipt, description);
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
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

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<OrderBook> getOrders() {
        return orderBooks;
    }

    public void setOrders(Set<OrderBook> orderBooks) {
        this.orderBooks = orderBooks;
    }
}
