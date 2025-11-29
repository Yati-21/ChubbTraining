package com.quiz.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.quiz.entity.Question;
import com.quiz.entity.Quiz;
import com.quiz.repository.QuizRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuizServiceImpl implements QuizService {

	private static final String QUESTION_SERVICE_CB = "questionServiceCB";

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
	@CircuitBreaker(name=QUESTION_SERVICE_CB, fallbackMethod="getAllQuizzesFallback")
	public List<Quiz> get() 
	{
		List<Quiz> quizzes =quizRepo.findAll();

		List<Quiz> newQuizList =quizzes.stream().map(quiz -> {
			log.info("Attempting to get questions for Quiz ID: {}",quiz.getId());
			quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
			return quiz;
		}).collect(Collectors.toList());

		return newQuizList;
	}

	//fallback method for above method CB
	public List<Quiz> getAllQuizzesFallback(Exception exception)
	{
		log.warn("Circuit Breaker activated for Quiz list retrieval. Reason: {}",exception.getMessage());
		//create a quiz to tell fallback state
		Quiz fallbackQuiz=new Quiz();
	    fallbackQuiz.setId("NA");
	    fallbackQuiz.setTitle("Question Service is not available!!!");
	    //add fallback message inside questions list
	    List<Question> fallbackQuestions=List.of(new Question("NA","Fallback: Question Service failed. Reason: "+exception.getMessage(),"NA"));
	    fallbackQuiz.setQuestions(fallbackQuestions);
	    return List.of(fallbackQuiz);
	}
	
	@Override
	@CircuitBreaker(name=QUESTION_SERVICE_CB,fallbackMethod="getQuizByIdFallback")
	public Quiz get(String id) {
		Quiz quiz=quizRepo.findById(id).orElseThrow(() ->new RuntimeException("QuizId not found"));
		quiz.setQuestions(questionClient.getQuestionsOfQuiz(quiz.getId()));
		return quiz;
	}
	//fallback method 
	public Quiz getQuizByIdFallback(String id, Exception exception) 
	{
		log.warn("Circuit breaker activated for 1 quiz get request (ID: {}). Reason: {}",id,exception.getMessage());
		Quiz quiz = quizRepo.findById(id).orElse(null);
		if (quiz != null) {
	        log.info("Base Quiz found (ID: {}). Returning it with empty/fallback questions.", id);
   	        List<Question> fallbackQuestions = List.of(new Question("NA", "Question Service is currently down. Reason: "+exception.getMessage(),id));
            quiz.setQuestions(fallbackQuestions);
	    } else {
            log.error("Quiz ID {} not found in local DB during fallback.", id);
            throw new RuntimeException("QuizId not found in local DB during fallback process.");
	    }
	    return quiz;
	}
	

}
