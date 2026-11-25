package br.edu.quiz.pattern;

import br.edu.quiz.model.Question;
import br.edu.quiz.model.Quiz;
import java.util.ArrayList;
import java.util.List;

public final class QuizBuilder {
    private String id;
    private String title;
    private String description = "";
    private final List<Question> questions = new ArrayList<>();
    public QuizBuilder id(String value) { id = value; return this; }
    public QuizBuilder title(String value) { title = value; return this; }
    public QuizBuilder description(String value) { description = value; return this; }
    public QuizBuilder addQuestion(Question value) { questions.add(value); return this; }
    public QuizBuilder questions(List<Question> values) { questions.clear(); questions.addAll(values); return this; }
    public Quiz build() { return new Quiz(id, title, description, questions); }
}
