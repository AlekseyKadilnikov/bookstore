package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

    @Query("from Book where dateOfReceipt < :date order by name asc")
    List<Book> getStateBooksOrderByNameAsc(@Param("date") LocalDate date);

    @Query("from Book where dateOfReceipt < :date order by name desc")
    List<Book> getStateBooksOrderByNameDesc(@Param("date") LocalDate date);

    @Query("from Book where dateOfReceipt < :date order by price asc")
    List<Book> getStateBooksOrderByPriceAsc(@Param("date") LocalDate date);

    @Query("from Book where dateOfReceipt < :date order by price desc")
    List<Book> getStateBooksOrderByPriceDesc(@Param("date") LocalDate date);
}
