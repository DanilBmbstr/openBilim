// src/main/java/com/openBilim/Tasks/SingleChoiceTask.java
package com.openBilim.Tasks;

import java.util.List;
import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;
import com.openBilim.HTTP_Handling.Router;

public class SingleChoiceTask extends Task {
    private final List<String> options; // варианты ответа: ["Москва", "Париж", "Лондон", "Берлин"]
    private final int correctIndex; // индекс правильного варианта (0-based)
    public String selectedOption; // что прислал пользователь (строка из options)

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
        if (selectedOption == null)
            return false;
        int selectedIndex = options.indexOf(selectedOption);
        return selectedIndex == correctIndex;
    }

    public void handleAnswer(String session_id,Consumer<AnswerData> resultCallback) {
        Router.handleSingleChoiseAnswer(session_id, this, result -> {
            resultCallback.accept(new AnswerData(result.userToken, result.answer, result.validation));
            ;

        });
    }
}
