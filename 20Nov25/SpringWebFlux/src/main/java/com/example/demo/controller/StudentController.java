package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/students")
public class StudentController 
{

    private final StudentService service;

    public StudentController(StudentService service) 
    {
        this.service = service;
    }

    @GetMapping
    public Flux<Student> getAll() 
    {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Student> getOne(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Student> create(@RequestBody Student student) {
        return service.create(student);
    }

    @PutMapping("/{id}")
    public Mono<Student> update(@PathVariable int id, @RequestBody Student student) 
    {
        return service.update(id, student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable int id) 
    {
        return service.delete(id);
    }
}
