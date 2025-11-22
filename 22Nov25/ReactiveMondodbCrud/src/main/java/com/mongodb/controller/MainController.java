package com.mongodb.controller;

import com.mongodb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.model.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


//mongodb://localhost:27017


@RestController
public class MainController 
{

	@Autowired
    private StudentService studentService;
	
	
	@PostMapping("/addStudent")
	public Mono<Void> addStudent(@RequestBody Student student)
	{
		return studentService.save(student).then();
	}
	
	

	//get all the students in db
	@GetMapping("/fetchStudents")
	public Flux<Student> fetchAllStudents()
	{
		return studentService.findAll();
	}	
	
	//get a perticular student using id
	@GetMapping("/fetchStudent/{id}")
	public Mono<Student> getStudentById(@PathVariable Integer id)
	{
		return studentService.findById(id);
	}
	
	@PutMapping("/updateStudent")
	public Mono<Void> updateStudent(@RequestBody Student student)
	{
		return studentService.update(student).then();
	}
	
	
	@DeleteMapping("/deleteStudent/{id}")
	public Mono<Void> deleteStudent(@PathVariable Integer id)
	{
		return studentService.deleteById(id);
		
	}
	

}
