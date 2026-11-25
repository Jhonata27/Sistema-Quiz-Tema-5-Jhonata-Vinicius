package br.edu.quiz.repository;

import br.edu.quiz.model.Quiz;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface QuizRepository {
    List<Quiz> findAll() throws IOException;
    Optional<Quiz> findById(String id) throws IOException;
    void save(Quiz quiz) throws IOException;
    boolean delete(String id) throws IOException;
}
