package br.edu.quiz.pattern;

public record QuizEvent(String quizTitle, int answered, int total, int correct, boolean finished) { }
