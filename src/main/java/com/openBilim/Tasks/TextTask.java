package com.openBilim.Tasks;

import java.util.List;
import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;



//Класс котоырй репрезентует задания, для которых требуется самостоятельный ввод правильного ответа в текстовое поле
public class TextTask extends Task {


    private String rightAnswer;

    public String answer;

    public TextTask(String id, String taskText, String rightAnswer, double points) {
        super(id, taskText,points);
        this.rightAnswer = rightAnswer;
    }

    public boolean validate() {

        return answer.toLowerCase().replaceAll(" ", "").equals(rightAnswer.toLowerCase().replaceAll(" ", ""));
    };

    @Override
    public void handleAnswer(String session_id, String user_id, Consumer<AnswerData> resultCallback) {
        router.handleTextAnswer(session_id, user_id, this, result -> {
            answerData.init(result.getUserId(), result.answer, result.validation);
            resultCallback.accept(answerData);
            
            ;

        });

    }

    public @Override List<String> getOptions() {
        System.err.println("method getOptions() is useless in TextTask class");
        return null;
    }
    public String getRightAnswer(){return rightAnswer;}

}
