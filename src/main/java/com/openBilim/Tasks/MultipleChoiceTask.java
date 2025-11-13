// src/main/java/com/openBilim/Tasks/MultipleChoiceTask.java
package com.openBilim.Tasks;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.openBilim.HTTP_Handling.AnswerData;
import com.openBilim.HTTP_Handling.Router;

public class MultipleChoiceTask extends Task {
    private final List<String> options;
    private final Set<Integer> correctIndices; // набор индексов правильных ответов
    public String selectedOptions; // что прислал пользователь (массив строк) 
    //изменил selectedOptions на String, не очень хорошая практика, но так будет гораздо проще. Временное решение

    public MultipleChoiceTask(String taskText, List<String> options, Set<Integer> correctIndices) {
        super(taskText);
        this.options = options;
        this.correctIndices = correctIndices;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public boolean validate() {
        if (selectedOptions == null || selectedOptions.isBlank()) {
            return false;
        }

        // Разбиваем строку по запятой (и убираем пробелы)
        Set<Integer> selectedIndices = parseSelectedOptions(selectedOptions);

        return selectedIndices.equals(correctIndices);
    }
/**
     * Умный парсер: поддерживает и индексы, и сами варианты ответа
     * Примеры входящих строк:
     *   "0, 2, 3"
     *   "Меркурий, Венера, Земля"
     *   "  Париж ,  Лондон  "
     */
/**
     * Универсальный парсер: принимает как индексы ("0, 2, 3"), так и текст вариантов.
     * Работает с лишними пробелами, разным регистром — всё как надо.
     */
    private Set<Integer> parseSelectedOptions(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::resolveToIndex)
                .filter(idx -> idx >= 0)
                .collect(Collectors.toSet());
    }
    /**
     * Пытается превратить строку в индекс:
     * - сначала как число
     * - потом как точное совпадение текста
     * - (по желанию можно добавить .equalsIgnoreCase() если нужен игнор регистра)
     */
    private int resolveToIndex(String option) {
        try {
            int idx = Integer.parseInt(option);
            if (idx >= 0 && idx < options.size()) {
                return idx;
            }
        } catch (NumberFormatException ignored) {
            // не число — ищем по тексту
        }

        int idx = options.indexOf(option);
        if (idx != -1) {
            return idx;
        }
        return -1; // не нашли
    }
    public void handleAnswer(String session_id,Consumer<AnswerData> resultCallback){
                    Router.handleMultipleChoiseAnswer(session_id, this, result -> {
                resultCallback.accept(new AnswerData(result.userToken, result.answer, result.validation));;
    
});
}
}
