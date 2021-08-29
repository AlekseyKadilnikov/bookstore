package com.alexeykadilnikov.entity;

import java.io.Serializable;
import java.util.List;

public class Author extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2032341091985408913L;
    private static long idCount = 0;

    private String firstName;
    private String lastName;
    private String middleName;

    private List<Long> books;

    public Author(String firstName, String lastName, String middleName) {
        super(idCount++);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public Author(String firstName, String lastName, String middleName, List<Long> books) {
        super(idCount++);
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.books = books;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public List<Long> getBooks() {
        return books;
    }

    public void setBooks(List<Long> books) {
        this.books = books;
    }
}
