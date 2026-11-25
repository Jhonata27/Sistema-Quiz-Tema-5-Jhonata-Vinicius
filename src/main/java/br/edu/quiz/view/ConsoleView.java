package br.edu.quiz.view;

import br.edu.quiz.controller.QuizController;
import br.edu.quiz.model.Question;
import br.edu.quiz.model.Quiz;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ConsoleView {
    private final QuizController controller;
    private final Scanner in = new Scanner(System.in);
    public ConsoleView(QuizController controller) { this.controller = controller; }
    public void run() {
        System.out.println("=== Quiz Padrões ===");
        boolean running = true;
        while (running) {
            System.out.println("\n1 Listar  2 Buscar  3 Criar  4 Editar  5 Remover  6 Executar  0 Sair");
            String choice = prompt("Opção: ");
            try {
                switch (choice) {
                    case "1" -> show(controller.list());
                    case "2" -> show(controller.search(required("Termo de busca: ")));
                    case "3" -> create();
                    case "4" -> edit();
                    case "5" -> remove();
                    case "6" -> play();
                    case "0" -> running = false;
                    default -> System.out.println("Opção inválida. Escolha um número do menu.");
                }
            } catch (IOException | IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Não foi possível concluir: " + ex.getMessage());
            }
        }
        System.out.println("Até mais!");
    }
    private void show(List<Quiz> quizzes) {
        if (quizzes.isEmpty()) { System.out.println("Nenhum quiz encontrado."); return; }
        quizzes.forEach(q -> System.out.printf("%s | %s (%d questões) - %s%n", q.id(), q.title(), q.questions().size(), q.description()));
    }
    private void create() throws IOException {
        String title = required("Título: "); String description = prompt("Descrição: ");
        System.out.println("1 Criar questões no terminal  2 Importar CSV de exemplo");
        List<Question> questions;
        if (prompt("Opção: ").equals("2")) {
            String path = required("Caminho do CSV: ");
            Quiz quiz = controller.importCsv(title, description, Path.of(path));
            System.out.println("Quiz criado: " + quiz.id()); return;
        }
        int count = number("Quantidade de questões (1-50): ", 1, 50); questions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("Questão " + (i + 1)); String prompt = required("Enunciado: ");
            int optionCount = number("Quantidade de alternativas (2-6): ", 2, 6); List<String> options = new ArrayList<>();
            for (int j = 0; j < optionCount; j++) options.add(required("Alternativa " + (j + 1) + ": "));
            int correct = number("Número da alternativa correta (1-" + optionCount + "): ", 1, optionCount) - 1;
            questions.add(new Question(prompt, options, correct));
        }
        System.out.println("Quiz criado: " + controller.create(title, description, questions).id());
    }
    private void edit() throws IOException {
        String id = required("ID do quiz: "); Quiz q = controller.get(id);
        if (q == null) { System.out.println("Quiz não encontrado."); return; }
        String title = prompt("Novo título [" + q.title() + "]: "); if (title.isBlank()) title = q.title();
        String description = prompt("Nova descrição [" + q.description() + "]: "); if (description.isBlank()) description = q.description();
        System.out.println(controller.edit(id, title, description) ? "Quiz atualizado." : "Quiz não encontrado.");
    }
    private void remove() throws IOException {
        String id = required("ID do quiz: ");
        if (controller.get(id) == null) { System.out.println("Quiz não encontrado."); return; }
        if (prompt("Confirma a remoção? (s/n): ").equalsIgnoreCase("s")) System.out.println(controller.remove(id) ? "Quiz removido." : "Quiz não encontrado.");
        else System.out.println("Remoção cancelada.");
    }
    private void play() throws IOException {
        String id = required("ID do quiz: "); Quiz q = controller.get(id);
        if (q == null) { System.out.println("Quiz não encontrado."); return; }
        System.out.println("\n" + q.title() + " — " + q.description()); List<Integer> answers = new ArrayList<>();
        for (int i = 0; i < q.questions().size(); i++) {
            Question question = q.questions().get(i); System.out.println("\n" + (i + 1) + ". " + question.prompt());
            for (int j = 0; j < question.options().size(); j++) System.out.println("  " + (j + 1) + ") " + question.options().get(j));
            answers.add(number("Resposta: ", 1, question.options().size()) - 1);
        }
        controller.play(q, answers, new ConsoleQuizObserver());
    }
    private int number(String label, int min, int max) {
        while (true) { try { int n = Integer.parseInt(prompt(label)); if (n >= min && n <= max) return n; }
            catch (NumberFormatException ignored) { }
            System.out.println("Digite um número entre " + min + " e " + max + "."); }
    }
    private String required(String label) { while (true) { String value = prompt(label); if (!value.isBlank()) return value.trim(); System.out.println("Este campo é obrigatório."); } }
    private String prompt(String label) { System.out.print(label); return in.nextLine(); }
}
