package com.openBilim.Session_Handling;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.*;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private String session_id;
    private Test test;
    private String user_token;
    private int taskIterator;
    Task current;
    public float score;
    private List<AnswerData> answers;

    public UserSession(String id, Test test, String user) {
        session_id = id;
        this.test = test;
        user_token = user;
        taskIterator = 0;
        answers = new ArrayList<>();
    }

    public void processTask() {
        
        current = test.getTask(taskIterator);

        if (current != null) {
            current.getRouter().sendNewTask(current, session_id);
            current.handleAnswer(session_id, user_token, result -> {
                if (result.getUser() != null) {

                    System.out.println("Пользователь " + result.getUser() + " ответил на вопрос \n правильность: "
                            + result.validation);
                    ++taskIterator;

                    answers.add(result);
                    if(result.validation) score += current.getPoints();
                    processTask();
                } else {
                    System.out.println("Произошла ошибка: неверный идентификатор пользователя");
                }

            });
        } else {
            System.err.println("Тест окончен!");
            Router r = new Router();
            r.sendTestResults(session_id, answers, score);

        }
 
    }

    public void run() {

        new Thread(() -> {
            System.err.println("Запущена сессия: " + session_id);
            processTask();
        }).start();
    }

}
