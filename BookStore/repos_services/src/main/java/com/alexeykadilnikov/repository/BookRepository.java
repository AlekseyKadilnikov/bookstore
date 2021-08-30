package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Request;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class BookRepository implements IBookRepository {

    private List<Book> books = new ArrayList<>();

    @Override
    public List<Book> findAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore", "root", "1111")) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                Book book = new Book();
                setBookFieldsFromResultSet(book, resultSet);
                books.add(book);
            }

            for(Book book : books) {
                resultSet = statement.executeQuery("SELECT * FROM author_book WHERE book_id = " + book.getId());
                List<Long> authorsId = new ArrayList<>();
                while (resultSet.next()) {
                    authorsId.add(resultSet.getLong("author_id"));
                }
                book.setAuthors(authorsId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Book getById(Long id) {
        Book book = new Book();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore", "root", "1111")) {
            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM book WHERE id = ?");
            prepStatement.setLong(1, id);
            ResultSet resultSet = prepStatement.executeQuery();
            resultSet.next();
            if (!resultSet.next()) {
                return null;
            }
            setBookFieldsFromResultSet(book, resultSet);

            Statement statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM author_book WHERE book_id = " + book.getId());
            List<Long> authorsId = new ArrayList<>();
            while (resultSet.next()) {
                authorsId.add(resultSet.getLong("author_id"));
            }
            book.setAuthors(authorsId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    @Override
    public void save(Book book) {
        books.add(book);
    }

    @Override
    public void delete(Book book) {
        books.remove(book);
    }

    @Override
    public void saveAll(List<Book> all) {
        books = all;
    }

    public void addRequest(Request request, int count, long id) {
        Book book = getById(id);
        List<Request> commonRequests = book.getCommonRequests();
        Request[] orderRequests = book.getOrderRequests();
        if(request.getStatus() == RequestStatus.COMMON) {
            for(Request r : commonRequests) {
                if(request.getName().equals(r.getName())) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
            commonRequests.add(request);
        }
        else {
            for(Request r : orderRequests) {
                if(request.getName().equals(r.getName()) && request.getStatus() == r.getStatus()) {
                    r.setCount(r.getCount() + count);
                    return;
                }
            }
            request.setCount(count);
        }
    }

    private void setBookFieldsFromResultSet(Book book, ResultSet resultSet) throws SQLException {
        book.setId(resultSet.getInt("id"));
        book.setName(resultSet.getString("name"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setPublicationYear(resultSet.getInt("year"));
        book.setCount(resultSet.getInt("count"));
        book.setPrice(resultSet.getInt("price"));
        book.setDescription(resultSet.getString("description"));
        book.setDateOfReceipt(LocalDate.parse(resultSet.getString("date_of_receipt")));
    }
}
