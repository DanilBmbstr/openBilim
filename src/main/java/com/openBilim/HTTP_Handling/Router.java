package com.openBilim.HTTP_Handling;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;

import java.util.List;
import java.util.function.Consumer;

import spark.Spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.Session_Handling.AvailableTests;
import com.openBilim.Session_Handling.Test;

import com.openBilim.Session_Handling.UnstartedTestDTO;
import com.openBilim.Tasks.*;

import com.openBilim.Users.Authorization.JWT_Util;

public class Router {



    public static synchronized void getAvailableTests(List<Test> testsList){
        unmap("/getAvailableTests");
        AvailableTests result = new AvailableTests();
        post("/getAvailableTests", (spark.Request req, spark.Response res) -> {
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
            TestResultDTO dto = new TestResultDTO(score);
            ObjectMapper mapper = new ObjectMapper();
            
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
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(null);
                return "Error: Wrong session";
            }

        });

    }


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
