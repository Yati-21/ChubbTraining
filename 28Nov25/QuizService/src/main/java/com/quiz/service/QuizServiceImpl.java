package com.quiz.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.entity.Quiz;
import com.quiz.repository.QuizRepository;

@Service
public class QuizServiceImpl implements QuizService {


	private QuizRepository quizRepo;
	private QuestionClient questionClient;
	
	public QuizServiceImpl(QuizRepository quizRepo, QuestionClient questionClient) {
		this.quizRepo = quizRepo;
		this.questionClient = questionClient;
	}

	@Override
	public Quiz add(Quiz quiz) {
		return quizRepo.save(quiz);
	}

	@Override
	public List<Quiz> get() {
		List<Quiz> quizzes = quizRepo.findAll();

		List<Quiz> newQuizList = quizzes.stream().map(quiz -> {
			quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
			return quiz;
		}).collect(Collectors.toList());

		return newQuizList;
	}

	@Override
	public Quiz get(String id) {
		Quiz quiz = quizRepo.findById(id).orElseThrow(() -> new RuntimeException("QuizId not found"));
		quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
		return quiz;
	}

}
