package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {

    @Query("select distinct r from Book as b inner join b.requests as r on b.id = :bookId order by r.name asc")
    List<Request> getRequestsForBookSortedByNameAsc(@Param("bookId") long bookId);

    @Query("select distinct r from Book as b inner join b.requests as r on b.id = :bookId order by r.name desc")
    List<Request> getRequestsForBookSortedByNameDesc(@Param("bookId") long bookId);

    @Query("select distinct r from Book as b inner join b.requests as r on b.id = :bookId order by r.count asc")
    List<Request> getRequestsForBookSortedByCountAsc(@Param("bookId") long bookId);

    @Query("select distinct r from Book as b inner join b.requests as r on b.id = :bookId order by r.count desc")
    List<Request> getRequestsForBookSortedByCountDesc(@Param("bookId") long bookId);
}
