package com.question.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.question.entity.Question;
import com.question.service.QuestionService;


@RestController
@RequestMapping("/question")
public class QuestionController 
{
	@Autowired
	private QuestionService questionService;
	
	@PostMapping
	public Question create(@RequestBody Question question)
	{
		return  questionService.create(question);
	}
	
	
	@GetMapping
	public List<Question> get()
	{
		return questionService.get();
	}

	@GetMapping("/{id}")
	public Question getOne(@PathVariable String id)
	{
		return questionService.get(id);
	}
	
	//get all questions of a prticular quiz
	@GetMapping("/quiz/{quizId}")
	public List<Question> getQuestionsOfQuiz(@PathVariable String quizId)
	{
		return questionService.getQuesOfQuiz(quizId);
	}

}
