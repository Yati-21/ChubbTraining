package com.question.service;

import java.util.List;

import com.question.entity.Question;


public interface QuestionService {
	Question create(Question question);
	List<Question> get();
	Question get(String id);
	List<Question> getQuesOfQuiz(String quizId);
}
