// src/main/java/com/openBilim/Tasks/SingleChoiceTask.java
package com.openBilim.Tasks;

import java.util.List;
import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;



public class SingleChoiceTask extends Task {
    private final List<String> options; // варианты ответа: ["Москва", "Париж", "Лондон", "Берлин"]
    private final int correctIndex; // индекс правильного варианта (0-based)
    public String selectedOption; // что прислал пользователь (строка из options)

    public SingleChoiceTask(String taskText, List<String> options, int correctIndex, double points) {
        super(taskText, points);
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public @Override List<String> getOptions() {
        return options;
    }

    @Override
    public boolean validate() {
        if (selectedOption == null)
            return false;
        int selectedIndex = Integer.parseInt(selectedOption);
        return selectedIndex == correctIndex;
    }

    public void handleAnswer(String session_id,String userToken,Consumer<AnswerData> resultCallback) {
        router.handleSingleChoiseAnswer(session_id,userToken, this, result -> {
            answerData.init(result.getUser(), result.answer, result.validation);
            resultCallback.accept(answerData);
            ;

        });
    }
    public String getRightAnswer(){return String.valueOf(correctIndex);}
}
