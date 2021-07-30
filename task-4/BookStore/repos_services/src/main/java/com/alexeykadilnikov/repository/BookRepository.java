package com.alexeykadilnikov.repository;



import com.alexeykadilnikov.entity.Book;

public class BookRepository implements IRepository<Book, Long> {
    private final Book[] books = new Book[] {
            new Book("Идиот", "Федор Достоевский", "Наука", 1990, 350, true),
            new Book("Бесы", "Федор Достоевский", "Наука", 1988, 370, true),
            new Book("Война и мир", "Лев Толстой", "Наука", 2009, 400,false),
            new Book("Палач", "Эдуард Лимонов", "Питер", 2019, 550, true),
            new Book("Черный обелиск", "Мария Ремарк", "Феникс", 1993, 310, false),
            new Book("Капитал", "Карл Маркс", "Питер", 2011, 660, true),
            new Book("Лолита", "Владимир Набоков", "Питер", 2000, 490, true),
            new Book("Сияние", "Стивен Кинг", "Питер", 2018, 500,false),
            new Book("1984", "Джордж Оруэлл", "Питер", 2010, 550, true)
    };

    @Override
    public Book[] findAll() {
        return books;
    }

    @Override
    public Book getById(Long id) {
        return null;
    }

    @Override
    public void save(Book book) {

    }

    @Override
    public void delete(Book book) {

    }

    public Book getByIndex(int index) {
        return books[index];
    }
}
