package com.alexeykadilnikov.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderBook> orderBooks;

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
}
