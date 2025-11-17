package com.openBilim.Session_Handling;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.*;



public class UserSession {
    private String session_id;
    private Test test;
    private String user_token;
    private int taskIterator;
    Task current;

        

    public UserSession(String id, Test test, String user) {
        session_id = id;
        this.test = test;
        user_token = user;
        taskIterator = 0;
    }

    

    public void run() {

        new Thread(() -> {
            System.err.println("Запущена сессия: " + session_id);
            current = test.getTask(taskIterator);

            if (current != null) {
                current.getRouter().sendNewTask(current, session_id);
                current.handleAnswer(session_id, user_token, result -> {
                    if (result.getUser() != null) {

                        System.out.println("Пользователь " + result.getUser() + " ответил на вопрос \n правильность: "
                                + result.validation);
                        ++taskIterator;
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
