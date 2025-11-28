package com.quiz.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quiz.entity.Question;



//this will call question service and get all the questions of a particular quiz

//@FeignClient(url="http://localhost:8082",value="Question-Client")   // this will call our service
@FeignClient(name = "QUESTION-SERVICE")
public interface QuestionClient {
	
	@GetMapping("/question/quiz/{quizId}")
	List<Question> getQuestionsOfQuiz(@PathVariable String quizId);
	
}
