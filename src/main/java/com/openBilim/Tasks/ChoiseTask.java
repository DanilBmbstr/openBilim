package com.openBilim.Tasks;

import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;

import com.openBilim.HTTP_Handling.Router;

public abstract class ChoiseTask extends Task{

    protected Router router; 
    protected final AnswerData answerData;
    //Само задание
    private String taskText;
    
    //Функция для проверки правильности ответа
    public abstract boolean validate();

    public abstract void handleAnswer(String session_id, String userToken, Consumer<AnswerData> resultCallback);


    public abstract String getRightAnswer();

    public Router getRouter(){return router;}
    public void updateRouter(){router = new Router();}
    
    public ChoiseTask(String taskText, double points){
        super(taskText, points);
        this.taskText = taskText;
        router = new Router();
        answerData = new AnswerData();
    }

    
    public String getTaskText(){
        return taskText;
    }






}
