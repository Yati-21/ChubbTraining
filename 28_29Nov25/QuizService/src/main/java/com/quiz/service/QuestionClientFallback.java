package com.quiz.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.quiz.entity.Question;

@Component
public class QuestionClientFallback implements QuestionClient {
    @Override
    public List<Question> getQuestionsOfQuiz(String quizId) {
        return List.of(
            new Question("NA", "Fallback: Question Service is DOWN", quizId)
        );
    }
}
