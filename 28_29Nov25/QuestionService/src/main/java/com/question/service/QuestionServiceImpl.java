package com.question.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.question.entity.Question;
import com.question.repository.QuestionRepository;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepo;
	
	@Override
	public Question create(Question question) {
		return questionRepo.save(question);
	}

	@Override
	public List<Question> get() {
		return questionRepo.findAll();
	}

	@Override
	public Question get(String id) {
		return questionRepo.findById(id).orElseThrow(()->new RuntimeException("question id not found"));
	}

	@Override
	public List<Question> getQuesOfQuiz(String quizId) {
		return questionRepo.findByQuizId(quizId);
	}

}
