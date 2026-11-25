# Quiz Padrões

Sistema de terminal em Java 17 para criar, pesquisar, editar, remover e executar quizzes. A pontuação é apresentada durante a sessão, e os quizzes ficam salvos em arquivo texto após o encerramento.

## Requisitos

- JDK 17 ou superior
- Windows PowerShell, Prompt de Comando, macOS ou Linux
- Não há bibliotecas externas

## Executar

Abra o terminal nesta pasta e execute:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object { $_.FullName })
java -cp out br.edu.quiz.Main
```

No macOS/Linux, compile com:

```sh
mkdir -p out
find src/main/java -name '*.java' -print0 | xargs -0 javac -encoding UTF-8 -d out
java -cp out br.edu.quiz.Main
```

O arquivo padrão de persistência é `data/quizzes.txt`, criado automaticamente. Para escolher outro local, informe-o ao iniciar:

```sh
java -cp out br.edu.quiz.Main caminho/para/dados.txt
```

O repositório usa registros de texto `Q` (quiz) e `P` (pergunta); os textos são codificados em Base64 UTF-8 para preservar acentos e delimitadores. O arquivo é gravado em substituição atômica quando o sistema salva alterações.

## Importar questões

Ao criar um quiz, escolha a opção de importação e informe o caminho de `exemplos/questoes-exemplo.csv`. O formato é separado por ponto e vírgula: `pergunta;alternativa1;alternativa2;...;correta`, e a alternativa correta é numerada a partir de 1. Para um CSV próprio, use UTF-8 e uma linha por questão. O adaptador ignora uma linha de cabeçalho iniciada por `pergunta;` e linhas iniciadas por `#`.

## Menu

1. Listar quizzes
2. Buscar por título ou descrição
3. Criar quiz manualmente ou importar questões CSV
4. Editar título e descrição
5. Remover quiz
6. Executar quiz e ver progresso/pontuação
0. Sair

O identificador exibido na listagem deve ser usado para editar, remover ou executar o quiz. A edição mantém as questões existentes.

## Camadas e padrões

- `model`: `Quiz` e `Question`, entidades imutáveis com validações.
- `repository`: interface `QuizRepository` e persistência em arquivo `FileQuizRepository`.
- `controller`: `QuizController`, regras e coordenação entre interface, padrões e repositório.
- `view`: `ConsoleView` e observador de eventos de pontuação.
- `pattern`: `QuizBuilder` (criacional), `LegacyCsvQuestionAdapter` (estrutural) e `QuizSession`/`QuizObserver` (comportamental).

O diagrama de classes, as justificativas e a divisão sugerida de tarefas estão no PDF `documentacao/Documentacao-Quiz-Padroes.pdf`.

## Exemplo de demonstração

1. Execute o programa e escolha `3`.
2. Informe o título `Quiz de padrões` e uma descrição curta.
3. Escolha `2` e informe `exemplos/questoes-exemplo.csv`.
4. Escolha `1` para copiar o identificador do quiz.
5. Escolha `6`, informe o identificador e responda às questões.
6. Encerre com `0`, inicie novamente e liste os quizzes para mostrar a persistência.

