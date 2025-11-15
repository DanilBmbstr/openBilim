package com.openBilim.Session_Handling;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.*;
import java.util.ArrayList;
import java.util.List;


public class UserSession {
    private final String session_id;
    private final List<Task> tasks;
    private final String user_token;
    private final List<AnswerData> results = new ArrayList<>();
    private int currentIndex = 0;

    public UserSession(String id, Test test, String user) {
        session_id = id;
        this.test = test;
        user_token = user;
        this.tasks = new ArrayList<>(test.getTasks()); 
    }

    public String getSesssion_id(){return session_id;}
    public String getUser_token(){return user_token;}
    
    public Task getCurrentTask() {
        return currentIndex < tasks.size() ? tasks.get(currentIndex) : null;
    }
    public boolean isFinished() {
        return currentIndex >= tasks.size();
    }
    public void submitAnswer(AnswerData result) {
        results.add(result);
        currentIndex++;  //  Переходим к следующему
    }
    
    public void run() {

        new Thread(() -> {
            System.err.println("Запущена сессия: " + session_id);
            current = test.popTask();

            if (current != null) {
                current.getRouter().sendNewTask(current, session_id);
                current.handleAnswer(session_id, user_token, result -> {
                    if (result.getUser() != null) {

                        System.out.println("Пользователь " + result.getUser() + " ответил на вопрос \n правильность: "
                                + result.validation);
                        this.run();
                    } else {
                        System.out.println("Произошла ошибка: неверный идентификатор пользователя");
                    }

                });
            }
            else {System.err.println("Тест окончен!"); 
            Router r = new Router();
            r.sendNewTask(null, session_id);
            
        }
            /*
             * Router.sendNewTask(current, session_id);
             * current.handleAnswer(session_id, user_token, result -> {
             * if(result.getUser()!= null){
             * System.out.println("Пользователь " + result.getUser() +
             * " ответил на вопрос \n правильность: "
             * + result.validation);
             * this.run();
             * }
             * else
             * {System.out.println("Произошла ошибка: неверный идентификатор пользователя");
             * }
             * });
             */
        }).start();
    }

}
