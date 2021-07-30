package com.alexeykadilnikov.service;

public interface IBookService {
    void setBookStatus(int index, boolean status);
    String showBook(int index);
}
