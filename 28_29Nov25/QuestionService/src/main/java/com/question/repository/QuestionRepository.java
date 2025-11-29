package com.question.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.question.entity.Question;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>{

	//to find all the ques of a quiz
	List<Question> findByQuizId(String quizId);
	
}
