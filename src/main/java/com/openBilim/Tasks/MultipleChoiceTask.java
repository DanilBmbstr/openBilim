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
    public List<String> selectedOptions; ; // что прислал пользователь (массив строк) 
    //изменил selectedOptions на String, не очень хорошая практика, но так будет гораздо проще. Временное решение

    public MultipleChoiceTask(String taskText, List<String> options, Set<Integer> correctIndices, double points) {
        super(taskText, points);
        this.options = options;
        this.correctIndices = correctIndices;
    }

    public @Override List<String> getOptions() {
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

    public void handleAnswer(String session_id,String userToken, Consumer<AnswerData> resultCallback) {
        router.handleMultipleChoiseAnswer(session_id, userToken,this, result -> {
            answerData.init(result.getUser(), result.answer, result.validation);
            resultCallback.accept(answerData);

        });
    }
    public String getRightAnswer(){
        System.out.println(correctIndices.toString());
        return correctIndices.toString();}
}