package com.openBilim.Tasks;

import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;
import com.openBilim.HTTP_Handling.Router;

//Класс котоырй репрезентует задания, для которых требуется самостоятельный ввод правильного ответа в текстовое поле
public class TextTask extends Task {
    private String taskText;

    private String rightAnswer;

    public String answer;

    public TextTask(String taskText, String rightAnswer) {
        super(taskText);
        this.rightAnswer = rightAnswer;
    }

    public boolean validate() {
        return answer.equals(rightAnswer);
    };

    public void handleAnswer(String session_id,Consumer<AnswerData> resultCallback) {
        Router.handleTextAnswer(session_id, this, result -> {
            resultCallback.accept(new AnswerData(result.userToken, result.answer, result.validation));
            ;

        });

    }
}
