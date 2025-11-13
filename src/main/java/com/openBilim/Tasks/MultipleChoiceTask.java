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
    public String selectedOptions; // что прислал пользователь (массив строк) 
    //изменил selectedOptions на String, не очень хорошая практика, но так будет гораздо проще. Временное решение

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
        /*  Требуется немного переработки под String вместо List<string>
        if (selectedOptions == null || selectedOptions.isEmpty())
            return false;

        Set<Integer> selectedIndices = selectedOptions.stream()
                .map(options::indexOf)
                .collect(Collectors.toSet());

        return selectedIndices.equals(correctIndices);
         */
        return false;
    }

    public void handleAnswer(String session_id,Consumer<AnswerData> resultCallback){
                    Router.handleMultipleChoiseAnswer(session_id, this, result -> {
                resultCallback.accept(new AnswerData(result.userToken, result.answer, result.validation));;
    
});
}
}