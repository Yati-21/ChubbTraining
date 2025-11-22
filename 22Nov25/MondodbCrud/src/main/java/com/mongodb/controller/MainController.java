package com.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import com.mongodb.model.Student;
import com.mongodb.repository.StudentRepository;


//mongodb://localhost:27017


@RestController
public class MainController 
{
	@Autowired
	StudentRepository studentRepository;
	
//	@PostMapping("/addStudent")
//	public Student addStudent(@RequestBody Student student)
//	{
//		return studentRepository.save(student);
//			
//	}

	
	@PostMapping("/addStudent")
	public void addStudent(@RequestBody Student student)
	{
		studentRepository.save(student);
			
	}

	
	@GetMapping("/fetchStudents")
	public List<Student> fetchAllStudents()
	{
		return studentRepository.findAll();
	}
	
	@GetMapping("/fetchStudent/{id}")
	public Student getStudentById(@PathVariable Integer id)
	{
		return studentRepository.findById(id).orElse(null);
	}
	
	@PutMapping("/updateStudent")
	public void updateStudent(@RequestBody Student student)
	{
		Student data = studentRepository.findById(student.getRollno()).orElse(null);
		if(data != null)
		{
			data.setName(student.getName());
			data.setAddress(student.getAddress());
			studentRepository.save(data);
		}
	}
	
	@DeleteMapping("/deleteStudent/{id}")
	public void deleteStudent(@PathVariable Integer id)
	{
		studentRepository.deleteById(id);
		
	}
	

}
