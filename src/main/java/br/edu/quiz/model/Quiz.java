package br.edu.quiz.model;

import java.util.List;
import java.util.UUID;

public record Quiz(String id, String title, String description, List<Question> questions) {
    public Quiz {
        id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Título obrigatório.");
        title = title.trim();
        description = description == null ? "" : description.trim();
        questions = List.copyOf(questions);
        if (questions.isEmpty()) throw new IllegalArgumentException("O quiz precisa ter ao menos uma questão.");
    }
    public Quiz withContent(String newTitle, String newDescription, List<Question> newQuestions) {
        return new Quiz(id, newTitle, newDescription, newQuestions);
    }
}
