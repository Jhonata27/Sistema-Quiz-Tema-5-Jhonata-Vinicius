package br.edu.quiz.controller;

import br.edu.quiz.model.Question;
import br.edu.quiz.model.Quiz;
import br.edu.quiz.pattern.LegacyCsvQuestionAdapter;
import br.edu.quiz.pattern.QuizBuilder;
import br.edu.quiz.pattern.QuizObserver;
import br.edu.quiz.pattern.QuizSession;
import br.edu.quiz.repository.QuizRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class QuizController {
    private final QuizRepository repository;
    private final LegacyCsvQuestionAdapter csvAdapter;
    public QuizController(QuizRepository repository, LegacyCsvQuestionAdapter csvAdapter) {
        this.repository = repository; this.csvAdapter = csvAdapter;
    }
    public List<Quiz> list() throws IOException { return repository.findAll(); }
    public List<Quiz> search(String term) throws IOException {
        String q = term.toLowerCase(Locale.ROOT);
        return repository.findAll().stream().filter(x -> x.title().toLowerCase(Locale.ROOT).contains(q)
                || x.description().toLowerCase(Locale.ROOT).contains(q)).toList();
    }
    public Quiz create(String title, String description, List<Question> questions) throws IOException {
        Quiz quiz = new QuizBuilder().title(title).description(description).questions(questions).build();
        repository.save(quiz); return quiz;
    }
    public Quiz importCsv(String title, String description, Path path) throws IOException {
        return create(title, description, csvAdapter.importQuestions(path));
    }
    public boolean edit(String id, String title, String description) throws IOException {
        Quiz old = repository.findById(id).orElse(null); if (old == null) return false;
        repository.save(old.withContent(title, description, old.questions())); return true;
    }
    public boolean remove(String id) throws IOException { return repository.delete(id); }
    public Quiz get(String id) throws IOException { return repository.findById(id).orElse(null); }
    public int play(Quiz quiz, List<Integer> answers, QuizObserver observer) {
        QuizSession session = new QuizSession(quiz); session.addObserver(observer);
        for (int answer : answers) session.answer(answer);
        return session.score();
    }
}
