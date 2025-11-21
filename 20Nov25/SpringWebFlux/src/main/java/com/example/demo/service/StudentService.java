package com.example.demo.service;


import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService 
{

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) 
    {
        this.repo = repo;
    }

    public Flux<Student> getAll() 
    {
        return repo.findAll();
    }

    public Mono<Student> getById(int id) 
    {
        return repo.findById(id);
    }

    public Mono<Student> create(Student student) 
    {
        return repo.save(student);
    }

    public Mono<Student> update(int id, Student updated) 
    {
        return repo.findById(id)
                .flatMap(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    return repo.save(existing);
                });
    }

    public Mono<Void> delete(int id) 
    {
        return repo.deleteById(id);
    }
}
