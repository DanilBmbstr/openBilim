package com.openBilim.Tasks;

public abstract class Task {

    //Само задание
    private String taskText;
    
    //Функция для проверки правильности ответа
    public abstract boolean validate();


    
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
