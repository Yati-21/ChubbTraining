package com.mongodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.model.Student;
import com.mongodb.repository.StudentRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService 
{
	@Autowired
	StudentRepository studentRepo;
	
	public Flux<Student> findAll()
	{
		return studentRepo.findAll();
	}
	
	public Mono<Student> findById(Integer id)
	{
		return studentRepo.findById(id);
	}
	
	public Mono<Student> save(Student student)
	{
		return studentRepo.save(student);
	}

	public Mono<Student> update(Student student)
	{

		return studentRepo.findById(student.getRollno())  //will work only if student to be updated exists
				.flatMap(existing ->{
					existing.setAddress(student.getAddress());
					existing.setName(student.getName());
						return studentRepo.save(existing);
				});
	}
	
	public Mono<Void> deleteById(Integer id)
	{
		return studentRepo.deleteById(id);
	}
}
