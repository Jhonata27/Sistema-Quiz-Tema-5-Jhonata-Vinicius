package br.edu.quiz.pattern;

import br.edu.quiz.model.Question;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface QuestionImportPort {
    List<Question> importQuestions(Path file) throws IOException;
}
