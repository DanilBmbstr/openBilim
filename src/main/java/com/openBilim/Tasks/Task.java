package com.openBilim.Tasks;

import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;

import com.openBilim.HTTP_Handling.Router;

public abstract class Task {

    protected final Router router; 
    protected final AnswerData answerData;
    //Само задание
    private String taskText;
    
    //Функция для проверки правильности ответа
    public abstract boolean validate();

    public abstract void handleAnswer(String session_id, Consumer<AnswerData> resultCallback);

    
    public Task(String taskText){
        this.taskText = taskText;
        router = new Router();
        answerData = new AnswerData();
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
