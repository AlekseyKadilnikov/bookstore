package com.alexeykadilnikov.entity;

import java.io.Serializable;
import java.util.Set;

public class Author extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2032341091985408913L;

    private String firstName;
    private String lastName;
    private String middleName;

    private Set<Long> booksId;

    public Author() {
    }

    public Author(String firstName, String lastName, String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public Author(String firstName, String lastName, String middleName, Set<Long> booksId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.booksId = booksId;
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

    public Set<Long> getBooksId() {
        return booksId;
    }

    public void setBooksId(Set<Long> booksId) {
        this.booksId = booksId;
    }
}
