package br.edu.quiz.repository;

import br.edu.quiz.model.Question;
import br.edu.quiz.model.Quiz;
import br.edu.quiz.pattern.QuizBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class FileQuizRepository implements QuizRepository {
    private final Path file;
    public FileQuizRepository(Path file) { this.file = file; }
    @Override public List<Quiz> findAll() throws IOException { return new ArrayList<>(read().values()); }
    @Override public Optional<Quiz> findById(String id) throws IOException { return Optional.ofNullable(read().get(id)); }
    @Override public void save(Quiz quiz) throws IOException {
        Map<String, Quiz> all = read(); all.put(quiz.id(), quiz); write(all);
    }
    @Override public boolean delete(String id) throws IOException {
        Map<String, Quiz> all = read(); boolean removed = all.remove(id) != null; if (removed) write(all); return removed;
    }
    private Map<String, Quiz> read() throws IOException {
        Map<String, Quiz> quizzes = new LinkedHashMap<>();
        if (!Files.exists(file)) return quizzes;
        Map<String, List<Question>> questions = new LinkedHashMap<>();
        Map<String, String[]> headers = new LinkedHashMap<>();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String[] c = lines.get(i).split("\\|", -1);
            try {
                if (c[0].equals("Q") && c.length == 4) { headers.put(c[1], new String[]{dec(c[2]), dec(c[3])}); questions.put(c[1], new ArrayList<>()); }
                else if (c[0].equals("P") && c.length >= 6) {
                    List<String> options = new ArrayList<>(); int count = Integer.parseInt(c[5]);
                    if (c.length != 6 + count) throw new IllegalArgumentException("número de alternativas inconsistente");
                    for (int j = 0; j < count; j++) options.add(dec(c[6 + j]));
                    List<Question> list = questions.get(c[1]); if (list == null) throw new IllegalArgumentException("quiz não encontrado");
                    list.add(new Question(c[2], dec(c[3]), options, Integer.parseInt(c[4])));
                } else throw new IllegalArgumentException("registro desconhecido");
            } catch (RuntimeException ex) { throw new IOException("Arquivo de dados inválido na linha " + (i + 1) + ": " + ex.getMessage(), ex); }
        }
        for (var e : headers.entrySet()) { String[] h = e.getValue(); quizzes.put(e.getKey(), new QuizBuilder().id(e.getKey()).title(h[0]).description(h[1]).questions(questions.get(e.getKey())).build()); }
        return quizzes;
    }
    private void write(Map<String, Quiz> quizzes) throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        List<String> lines = new ArrayList<>();
        for (Quiz q : quizzes.values()) {
            lines.add(String.join("|", "Q", q.id(), enc(q.title()), enc(q.description())));
            for (Question p : q.questions()) {
                List<String> row = new ArrayList<>(List.of("P", q.id(), p.id(), enc(p.prompt()), String.valueOf(p.correctIndex()), String.valueOf(p.options().size())));
                p.options().forEach(o -> row.add(enc(o))); lines.add(String.join("|", row));
            }
        }
        Path temp = file.resolveSibling(file.getFileName() + ".tmp");
        Files.write(temp, lines, StandardCharsets.UTF_8);
        try { Files.move(temp, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE); }
        catch (java.nio.file.AtomicMoveNotSupportedException ex) { Files.move(temp, file, java.nio.file.StandardCopyOption.REPLACE_EXISTING); }
    }
    private static String enc(String value) { return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8)); }
    private static String dec(String value) { return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8); }
}
