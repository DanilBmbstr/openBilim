// src/main/java/com/openBilim/Tasks/SingleChoiceTask.java
package com.openBilim.Tasks;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.openBilim.LOGGER;
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

    public SingleChoiceTask(String taskText, java.sql.Array options, int correctIndex, double points) {

        super(taskText, points);
        String optionsString = options.toString();

        List<String> options_tmp = null;
        int correctIndex_tmp = -1;
        try {

            optionsString = optionsString.substring(1, optionsString.length() - 1);

            String[] optionsStringArray = optionsString.split(", ");
            for (int i = 0; i < optionsStringArray.length; i++) {
                optionsStringArray[i] = optionsStringArray[i].substring(1, optionsStringArray[i].length() - 1);
            }

            if (optionsStringArray != null) {

                options_tmp = Arrays.asList(optionsStringArray);
                correctIndex_tmp = correctIndex;
            }
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());

        }
        this.options = options_tmp;
        this.correctIndex = correctIndex_tmp;

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

    public void handleAnswer(String session_id, String user_id, Consumer<AnswerData> resultCallback) {
        router.handleSingleChoiseAnswer(session_id, user_id, this, result -> {
            answerData.init(result.getUserId(), result.answer, result.validation);
            resultCallback.accept(answerData);
            ;

        });
    }

    public String getRightAnswer() {
        return String.valueOf(correctIndex);
    }
}
