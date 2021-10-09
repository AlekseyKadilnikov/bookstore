package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    @Query("from Order where executionDate >= :startDate and executionDate <= :endDate order by executionDate asc")
    List<Order> sortByExecDateForPeriodByDateAsc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("from Order where executionDate >= :startDate and executionDate <= :endDate order by executionDate desc")
    List<Order> sortByExecDateForPeriodByDateDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("from Order where executionDate >= :startDate and executionDate <= :endDate order by totalPrice desc")
    List<Order> sortByExecDateForPeriodByPriceAsc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("from Order where executionDate >= :startDate and executionDate <= :endDate order by totalPrice desc")
    List<Order> sortByExecDateForPeriodByPriceDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("from Order where status = :statusCode")
    List<Order> sortByStatus(@Param("statusCode") int statusCode);

    @Query("from Order where executionDate >= :startDate and executionDate <= :endDate")
    List<Order> getCompleteOrdersForPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
