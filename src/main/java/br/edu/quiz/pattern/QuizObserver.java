package br.edu.quiz.pattern;

@FunctionalInterface
public interface QuizObserver {
    void onQuizEvent(QuizEvent event);
}
