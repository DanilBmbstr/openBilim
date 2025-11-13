package com.openBilim.Session_Handling;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.*;

public class UserSession{
    private String session_id;
    private Test test;
    private String user_token;

    public UserSession(String id, Test test, String user) {
        session_id = id;
        this.test = test;
        user_token = user;
    }

    public void run() {
        new Thread(() -> {
            System.err.println("Запущена сессия: "+ session_id);
            Task current = test.popTask();
            //Router.sendNewTask(current);
            current.handleAnswer(session_id, result -> {
                System.out.println("Пользователь " + result.userToken + " ответил на вопрос \n правильность: "
                        + result.validation);
            });
        }).start();
    }

}
