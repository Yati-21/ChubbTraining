package com.mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.model.Student;

@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, Integer>
{

}
