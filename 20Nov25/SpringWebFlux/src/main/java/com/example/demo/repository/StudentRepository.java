package com.example.demo.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import com.example.demo.model.Student;
import reactor.core.publisher.Flux;

public interface StudentRepository extends R2dbcRepository<Student, Integer> {

    Flux<Student> findByNameContaining(String keyword);
}
