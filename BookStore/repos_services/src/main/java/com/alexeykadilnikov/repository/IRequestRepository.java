package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {
}
