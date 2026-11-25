package br.edu.quiz.model;

import java.util.List;
import java.util.UUID;

public record Question(String id, String prompt, List<String> options, int correctIndex) {
    public Question {
        id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        prompt = requireText(prompt, "Enunciado");
        options = List.copyOf(options);
        if (options.size() < 2) throw new IllegalArgumentException("A questão precisa de pelo menos duas alternativas.");
        if (options.stream().anyMatch(s -> s == null || s.isBlank())) throw new IllegalArgumentException("As alternativas não podem ficar vazias.");
        if (correctIndex < 0 || correctIndex >= options.size()) throw new IllegalArgumentException("Alternativa correta inválida.");
    }
    public Question(String prompt, List<String> options, int correctIndex) { this(null, prompt, options, correctIndex); }
    private static String requireText(String s, String name) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(name + " obrigatório.");
        return s.trim();
    }
}
