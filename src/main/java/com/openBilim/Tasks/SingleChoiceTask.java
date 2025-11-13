// src/main/java/com/openBilim/Tasks/SingleChoiceTask.java
package com.openBilim.Tasks;

import java.util.List;

public class SingleChoiceTask extends Task {
    private final List<String> options;        // варианты ответа: ["Москва", "Париж", "Лондон", "Берлин"]
    private final int correctIndex;            // индекс правильного варианта (0-based)
    public String selectedOption;              // что прислал пользователь (строка из options)

    public SingleChoiceTask(String taskText, List<String> options, int correctIndex) {
        super(taskText);
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    public boolean validate() {
        if (selectedOption == null) return false;
        int selectedIndex = options.indexOf(selectedOption);
        return selectedIndex == correctIndex;
    }
}
