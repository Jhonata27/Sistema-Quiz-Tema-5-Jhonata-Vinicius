package br.edu.quiz.pattern;

import br.edu.quiz.model.Quiz;
import java.util.ArrayList;
import java.util.List;

public final class QuizSession {
    private final Quiz quiz;
    private final List<QuizObserver> observers = new ArrayList<>();
    private int answered;
    private int correct;
    public QuizSession(Quiz quiz) { this.quiz = quiz; }
    public void addObserver(QuizObserver observer) { observers.add(observer); }
    public void answer(int selectedIndex) {
        if (answered >= quiz.questions().size()) throw new IllegalStateException("Esta sessão já terminou.");
        if (selectedIndex == quiz.questions().get(answered).correctIndex()) correct++;
        answered++;
        publish(answered == quiz.questions().size());
    }
    private void publish(boolean finished) {
        QuizEvent event = new QuizEvent(quiz.title(), answered, quiz.questions().size(), correct, finished);
        observers.forEach(o -> o.onQuizEvent(event));
    }
    public int score() { return correct; }
    public int total() { return quiz.questions().size(); }
}
