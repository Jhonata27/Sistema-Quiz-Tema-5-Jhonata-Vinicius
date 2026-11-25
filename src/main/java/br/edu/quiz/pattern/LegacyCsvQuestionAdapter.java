package br.edu.quiz.pattern;

import br.edu.quiz.model.Question;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class LegacyCsvQuestionAdapter implements QuestionImportPort {
    @Override public List<Question> importQuestions(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Question> result = new ArrayList<>();
        for (int line = 0; line < lines.size(); line++) {
            String raw = lines.get(line).trim();
            if (raw.isEmpty() || raw.startsWith("#") || raw.toLowerCase().startsWith("pergunta;")) continue;
            String[] cells = raw.split(";", -1);
            if (cells.length < 4) throw new IOException("Linha " + (line + 1) + ": use pergunta;alternativa1;alternativa2;...;índiceCorreto.");
            try {
                String prompt = cells[0].trim();
                int correct = Integer.parseInt(cells[cells.length - 1].trim()) - 1;
                List<String> options = new ArrayList<>();
                for (int i = 1; i < cells.length - 1; i++) options.add(cells[i].trim());
                result.add(new Question(prompt, options, correct));
            } catch (RuntimeException ex) {
                throw new IOException("Questão inválida na linha " + (line + 1) + ": " + ex.getMessage(), ex);
            }
        }
        if (result.isEmpty()) throw new IOException("O arquivo não contém questões.");
        return result;
    }
}
