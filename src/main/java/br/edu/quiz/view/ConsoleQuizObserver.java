package br.edu.quiz.view;

import br.edu.quiz.pattern.QuizEvent;
import br.edu.quiz.pattern.QuizObserver;

public final class ConsoleQuizObserver implements QuizObserver {
    @Override public void onQuizEvent(QuizEvent event) {
        if (event.finished()) System.out.printf("Sessão concluída: %d/%d pontos.%n", event.correct(), event.total());
        else System.out.printf("Progresso: %d/%d questões.%n", event.answered(), event.total());
    }
}
