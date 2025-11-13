package com.openBilim.Tasks;

import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;

public abstract class Task {

    //Само задание
    private String taskText;
    
    //Функция для проверки правильности ответа
    public abstract boolean validate();

    public abstract void handleAnswer(String session_id, Consumer<AnswerData> resultCallback);

    
    public Task(String taskText){
        this.taskText = taskText;
    }

    public String getTaskText(){
        return taskText;
    }


/* 
 * Абстрактный класс ответа
 * 
 * Могут быть разные типы ответов поэтому мы используем вложенный класс
*/




}
