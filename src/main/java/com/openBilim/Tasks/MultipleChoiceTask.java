// src/main/java/com/openBilim/Tasks/MultipleChoiceTask.java
package com.openBilim.Tasks;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MultipleChoiceTask extends Task {
    private final List<String> options;
    private final Set<Integer> correctIndices;   // набор индексов правильных ответов
    public List<String> selectedOptions;         // что прислал пользователь (массив строк)

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
        if (selectedOptions == null || selectedOptions.isEmpty()) return false;

        Set<Integer> selectedIndices = selectedOptions.stream()
                .map(options::indexOf)
                .collect(Collectors.toSet());

        return selectedIndices.equals(correctIndices);
    }
}
