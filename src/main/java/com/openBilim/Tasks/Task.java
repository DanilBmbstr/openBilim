package com.openBilim.Tasks;



import java.util.List;
import java.util.function.Consumer;

import com.openBilim.HTTP_Handling.AnswerData;

import com.openBilim.HTTP_Handling.Router;

public abstract class Task {

    protected Router router; 
    protected final AnswerData answerData;
    //Само задание
    private String taskText;
    private double points;
    //Функция для проверки правильности ответа
    public abstract boolean validate();

    public abstract void handleAnswer(String session_id, String user_id, Consumer<AnswerData> resultCallback);


    public abstract String getRightAnswer();

    public abstract List<String> getOptions();

    public double getPoints(){
        return points;
    }

    public Router getRouter(){return router;}
    public void updateRouter(){router = new Router();}
    
    public Task(String taskText, double points){
        this.taskText = taskText;
        router = new Router();
        answerData = new AnswerData();
        this.points = points;
    }

    
    public String getTaskText(){
        return taskText;
    }






}
