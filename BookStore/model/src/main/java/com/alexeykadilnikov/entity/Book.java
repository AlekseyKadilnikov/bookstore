package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.RequestStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Book extends BaseEntity {
    private static long idCount = 0;
    private String name;
    private String author;
    private String publisher;
    private int publicationYear;
    private int price;
    private int count;
    private Date dateOfReceipt = new Date();
    private String description = "";
    private List<Request> commonRequests = new ArrayList<>();
    private List<Request> orderRequests = new ArrayList<>();


    public Book(String name, String author, String publisher, int publicationYear, int price, int count) {
        super(idCount++);
        this.name = name;
        this.author = author;
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
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", count=" + count +
                '}';
    }

    public List<Request> getCommonRequests() {
        return commonRequests;
    }

    public List<Request> getOrderRequests() {
        return orderRequests;
    }

    public void addRequest(Request request) {
        if(request.getStatus() == RequestStatus.COMMON) {
            for(Request r : commonRequests) {
                if(request.getName().equals(r.getName())) {
                    r.setCount(r.getCount() + 1);
                    return;
                }
            }
            commonRequests.add(request);
        }
        else {
            for(Request r : orderRequests) {
                if(request.getName().equals(r.getName())) {
                    r.setCount(r.getCount() + 1);
                    return;
                }
            }
            orderRequests.add(request);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(Date dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
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
