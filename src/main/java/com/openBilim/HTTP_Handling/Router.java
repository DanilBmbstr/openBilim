package com.openBilim.HTTP_Handling;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import spark.Spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.DB_Utul;
import com.openBilim.LOGGER;
import com.openBilim.Session_Handling.AvailableTests;
import com.openBilim.Session_Handling.SessionCreator;
import com.openBilim.Session_Handling.SessionDTO;
import com.openBilim.Session_Handling.Test;
import com.openBilim.Session_Handling.TestDTO;
import com.openBilim.Session_Handling.UnstartedTestDTO;
import com.openBilim.Tasks.*;

import com.openBilim.Users.Authorization.JWT_Util;

public class Router {



    public static synchronized void getAvailableTests(){

        



        unmap("/getAvailableTests");
        AvailableTests result = new AvailableTests();
        post("/getAvailableTests", (spark.Request req, spark.Response res) -> {
            List<Test> testsList = DB_Utul.getAvailableTests("0");
            ObjectMapper objectMapper = new ObjectMapper();
            if (JWT_Util.validate(req.body()) != null){
                result.tests = new UnstartedTestDTO[testsList.size()];
                for(int i = 0; i < testsList.size(); i++)
                {
                    result.tests[i] = new UnstartedTestDTO(testsList.get(i).getSubject(),testsList.get(i).getName(), testsList.get(i).getTasksNumber(), testsList.get(i).getId());
                }
            return objectMapper.writeValueAsString(result);}
            else {return "Invalid Token";}
            
        });
        

    }


    public static synchronized void getUnfinishedTests(){
        unmap("/getUnfinishedTests");
        post("/getUnfinishedTests", (req, res) -> {
            if (JWT_Util.validate(req.body()) != null){
                for(int i = 0; i < SessionCreator.sessionList.size(); ++i)
                {
                    if(SessionCreator.sessionList.get(i).isFinished() == false);
                    {
                         if(SessionCreator.sessionList.get(i).getUserId().equals(JWT_Util.validateAndGetUserId(req.body())) ){
                        LOGGER.info(JWT_Util.validateAndGetUserId(req.body()) + " Продолжил прохождение теста на сессии " +SessionCreator.sessionList.get(i).getUserId());

                        ObjectMapper objectMapper = new ObjectMapper();
                        TestDTO result = new TestDTO(SessionCreator.sessionList.get(i).getTest().getSubject(),SessionCreator.sessionList.get(i).getTest().getName(), SessionCreator.sessionList.get(i).getTest().getTasksNumber(),SessionCreator.sessionList.get(i).get_id());
                        return objectMapper.writeValueAsString(result);}
                        }
                    }
                    return "No unfinished sessions!";
                }
            
            else {return "Invalid Token";}
            });
    }

    public synchronized void sendNewTask(Task task, String session_id) {
        unmap("/" + session_id + "/getTask");
        if (task != null) {
            get("/" + session_id + "/getTask", (req, res) -> {
                ObjectMapper taskMapper = new ObjectMapper();
                String json = null;
                if (task.getClass() == MultipleChoiceTask.class) {
                    ChoiseTaskDTO dto = new ChoiseTaskDTO("MultipleChoise", task.getTaskText(), task.getOptions());
                    json = taskMapper.writeValueAsString(dto);
                }
                else if(task.getClass() == SingleChoiceTask.class){
                    ChoiseTaskDTO dto = new ChoiseTaskDTO("SingleChoise", task.getTaskText(), task.getOptions());
                    json = taskMapper.writeValueAsString(dto);
                } 
                else if (task.getClass() == TextTask.class) {
                    TextTaskDTO dto = new TextTaskDTO(task.getTaskText());
                    json = taskMapper.writeValueAsString(dto);
                }
                return json;
            });
        } else {
            get("/" + session_id + "/getTask", (req, res) -> {
                return "Тест окончен";
            });
        }

    }

    // Имеет такой же эндпоинт как и sendNewTask но вызывается когда задания
    // закончились
    public synchronized void sendTestResults(String session_id, List<AnswerData> answers, float score) {
       unmap("/" + session_id + "/getTask");
        get("/" + session_id + "/getTask", (req, res) -> {
            
            TestResultDTO dto = new TestResultDTO(score, SessionCreator.getExistingSession(session_id).getTest().getMaxScore());
            ObjectMapper mapper = new ObjectMapper();
            
            for(int i = 0; i < SessionCreator.sessionList.size(); i++)
            {
                if(SessionCreator.sessionList.get(i).get_id().equals(session_id));

                DB_Utul.finish_session(Integer.parseInt(SessionCreator.sessionList.get(i).get_id()), score);

                SessionCreator.sessionList.remove(i);
           }

            return mapper.writeValueAsString(dto);
        });
    }

    // Для разных типов ответа используем разные эндпоинты
    // Колбэк используется для обработки результата
    public synchronized void handleTextAnswer(String session_id, String user_id, TextTask task,
            Consumer<AnswerData> resultCallback) {
                
     unmap("/" + session_id + "/textAns/ans");
     unmap("/" + session_id + "/singleChoise/ans");
     unmap("/" + session_id + "/multiChoise/ans");
        
        post("/" + session_id + "/textAns/ans", (spark.Request req, spark.Response res) -> {
            
            ObjectMapper objectMapper = new ObjectMapper();

            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);

            boolean eval;

            task.answer = answer.getAnswer();

            if (JWT_Util.validateAndGetUserId(answer.getUserToken()).equals(user_id)) {
                eval = task.validate();
                resultCallback.accept(new AnswerData(JWT_Util.validateAndGetUserId(answer.getUserToken()), answer.answer, eval));
                DB_Utul.register_answer(Integer.parseInt(session_id), Integer.parseInt(task.getId()), answer.getAnswer(), eval, (float)task.getPoints());
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(new AnswerData(null, "Wrong user", false));
                return "Error: Wrong session";
            }

        });
    }

    public synchronized void handleSingleChoiseAnswer(String session_id, String user_id, SingleChoiceTask task,
            Consumer<AnswerData> resultCallback) {
 unmap("/" + session_id + "/textAns/ans");
 unmap("/" + session_id + "/singleChoise/ans");
 unmap("/" + session_id + "/multiChoise/ans");
        post("/" + session_id + "/singleChoise/ans", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;
            task.selectedOption = answer.getAnswer();
            if (JWT_Util.validateAndGetUserId(answer.getUserToken()).equals(user_id)) {
                eval = task.validate();
                resultCallback.accept(new AnswerData(JWT_Util.validateAndGetUserId(answer.getUserToken()), answer.answer, eval));
                DB_Utul.register_answer(Integer.parseInt(session_id), Integer.parseInt(task.getId()), answer.getAnswer(), eval, (float)task.getPoints());
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(null);
                return "Error: Wrong session";
            }

        });

    }

    public synchronized void handleMultipleChoiseAnswer(String session_id, String user_id, MultipleChoiceTask task,
            Consumer<AnswerData> resultCallback) {
 unmap("/" + session_id + "/textAns/ans");
 unmap("/" + session_id + "/singleChoise/ans");
 unmap("/" + session_id + "/multiChoise/ans");
        post("/" + session_id + "/multiChoise/ans", (spark.Request req, spark.Response res) -> {

            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;

            task.selectedOptions = AnswerRequest.parceMultipleChoise(answer.answer);

            if (JWT_Util.validateAndGetUserId(answer.getUserToken()).equals(user_id)) {

                eval = task.validate();
                resultCallback.accept(new AnswerData(JWT_Util.validateAndGetUserId(answer.getUserToken()), answer.answer, eval));
                DB_Utul.register_answer(Integer.parseInt(session_id), Integer.parseInt(task.getId()), answer.getAnswer(), eval, (float)task.getPoints());
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(null);
                return "Error: Wrong session";
            }

        });

    }

//Перезагружаем настройки CORS после каждогог unmap
//почему-то unmap их сбрасывает
    private static void unmap(String path){
        Spark.unmap(path); 
               options("/*",
        (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            return "OK";
        });

before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
}
}
