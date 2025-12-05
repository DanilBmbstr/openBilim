// src/main/java/com/openBilim/Tasks/MultipleChoiceTask.java
package com.openBilim.Tasks;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.openBilim.LOGGER;
import com.openBilim.HTTP_Handling.AnswerData;

public class MultipleChoiceTask extends Task {
    private final List<String> options;
    private final Set<Integer> correctIndices; // набор индексов правильных ответов
    public List<String> selectedOptions;; // что прислал пользователь (массив строк)
    // изменил selectedOptions на String, не очень хорошая практика, но так будет
    // гораздо проще. Временное решение

    public MultipleChoiceTask(String id, String taskText, List<String> options, Set<Integer> correctIndices, double points) {
        super(id, taskText, points);
        this.options = options;
        this.correctIndices = correctIndices;
    }

    public MultipleChoiceTask(String id, String taskText, java.sql.Array options, java.sql.Array correctIndices, double points) {
        super(id, taskText, points);

        String[] optionsString = options.toString().substring(1, options.toString().length() - 1).split(", ");

        for (int i = 0; i < optionsString.length; i++) {
            optionsString[i] = optionsString[i].substring(1, optionsString[i].length() - 1);
        }
        this.options = Arrays.asList(optionsString);

        String[] correctsString = correctIndices.toString().substring(1, correctIndices.toString().length() - 1).split(",");

        Set<Integer> correctInts = new HashSet<>();
        
        for (int i = 0; i < correctsString.length; i++) {
            correctInts.add(Integer.parseInt(correctsString[i])); 
  
        }
        this.correctIndices = correctInts;

    }

    public @Override List<String> getOptions() {
        return options;
    }

    @Override
    public boolean validate() {
        if (selectedOptions == null || selectedOptions.isEmpty())
            return false;

        Set<Integer> selectedIndices = new HashSet<>();

        for (int i = 0; i < selectedOptions.size(); i++) {
            selectedIndices.add(Integer.parseInt(selectedOptions.get(i)));
        }
        return selectedIndices.equals(correctIndices);

    }

    public void handleAnswer(String session_id, String user_id, Consumer<AnswerData> resultCallback) {
        router.handleMultipleChoiseAnswer(session_id, user_id, this, result -> {
            answerData.init(result.getUserId(), result.answer, result.validation);
            resultCallback.accept(answerData);

        });
    }

    public String getRightAnswer() {
        System.out.println(correctIndices.toString());
        return correctIndices.toString();
    }
}
