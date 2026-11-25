package br.edu.quiz;

import br.edu.quiz.controller.QuizController;
import br.edu.quiz.pattern.LegacyCsvQuestionAdapter;
import br.edu.quiz.repository.FileQuizRepository;
import br.edu.quiz.view.ConsoleView;
import java.nio.file.Path;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        Path dataFile = args.length > 0 ? Path.of(args[0]) : Path.of("data", "quizzes.txt");
        var repository = new FileQuizRepository(dataFile);
        var controller = new QuizController(repository, new LegacyCsvQuestionAdapter());
        new ConsoleView(controller).run();
    }
}
