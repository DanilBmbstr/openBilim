// src/main/java/com/openBilim/Tasks/MultipleChoiceTask.java
package com.openBilim.Tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


import com.openBilim.HTTP_Handling.AnswerData;


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

        
        Set<Integer> selectedIndices = new HashSet<>(); 

        for(int i = 0; i < selectedOptions.size(); i++)
        {
            selectedIndices.add(Integer.parseInt(selectedOptions.get(i)));
        }
        return selectedIndices.equals(correctIndices);
        
    }

    public void handleAnswer(String session_id,String user_id, Consumer<AnswerData> resultCallback) {
        router.handleMultipleChoiseAnswer(session_id, user_id,this, result -> {
            answerData.init(result.getUserId(), result.answer, result.validation);
            resultCallback.accept(answerData);

        });
    }
    public String getRightAnswer(){
        System.out.println(correctIndices.toString());
        return correctIndices.toString();}
}
