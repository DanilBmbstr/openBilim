package com.openBilim.Session_Handling;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.*;

import java.util.ArrayList;
import java.util.List;
import com.openBilim.LOGGER;
public class UserSession {



    private String session_id;
    private Test test;
    private String user_id;
    private int taskIterator;
    Task current;
    public float score;
    private List<AnswerData> answers;

    public UserSession(String id, Test test, String user) {
        session_id = id;
        this.test = test;
        user_id = user;
        taskIterator = 0;
        answers = new ArrayList<>();
    }

    public synchronized String get_id(){
        return session_id;
    }

    public void processTask() {
        
        current = test.getTask(taskIterator);

        if (current != null) {
            current.getRouter().sendNewTask(current, session_id);
            current.handleAnswer(session_id, user_id, result -> {
                if (result.getUserId() != null) {

                    LOGGER.info("Пользователь " + result.getUserId() + " ответил на вопрос \n правильность: "
                            + result.validation);
                    ++taskIterator;

                    answers.add(result);
                    if(result.validation) score += current.getPoints();
                    processTask();
                } else {
                    LOGGER.warning("Произошла ошибка: неверный идентификатор пользователя");
                }

            });
        } else {
            LOGGER.info("Тест окончен!");
            Router r = new Router();
            r.sendTestResults(session_id, answers, score);

        }
 
    }

    public void run() {

        new Thread(() -> {
            LOGGER.info("Запущена сессия: " + session_id);
            processTask();
        }).start();
    }

}
