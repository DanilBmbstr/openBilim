package com.openBilim.HTTP_Handling;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.function.Consumer;

import spark.Spark;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.*;
import com.openBilim.Tasks.*;

import java.time.LocalTime;

public class Router {

    
    public void sendNewTask(Task task, String session_id) {
        Spark.unmap("/" + session_id + "/getTask");

        if(task != null) {
        get("/" + session_id + "/getTask", (req, res) -> {
               System.out.println(task.getTaskText());
            return task.getTaskText(); 
        });
        }
        else {
            get("/" + session_id + "/getTask", (req, res) -> {
            return "Тест окончен"; 
        });
        }
     
    }

    // Для разных типов ответа используем разные эндпоинты
    // Колбэк используется для обработки результата
    public void handleTextAnswer(String session_id, String userToken, TextTask task,
            Consumer<AnswerData> resultCallback) {
                Spark.unmap("/" + session_id + "/textAns/ans");
        post("/" + session_id + "/textAns/ans", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();

            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);

            boolean eval;

            task.answer = answer.getAnswer();

            if (answer.getUser().equals(userToken)) {
                eval = task.validate();
                resultCallback.accept(new AnswerData(answer.getUser(), answer.answer, eval));
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(new AnswerData(null, "Wrong user", false));
                return "Error: Wrong session";
            }

        });
    }

    public void handleSingleChoiseAnswer(String session_id, String userToken, SingleChoiceTask task,
            Consumer<AnswerData> resultCallback) {
                Spark.unmap("/" + session_id + "/singleChoise/ans");
        post("/" + session_id + "/singleChoise/ans", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;
            task.selectedOption = answer.getAnswer();
            if (answer.getUser().equals(userToken)) {
                eval = task.validate();
                resultCallback.accept(new AnswerData(answer.getUser(), answer.answer, eval));
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(null);
                return "Error: Wrong session";
            }

        });

    }

    public void handleMultipleChoiseAnswer(String session_id, String userToken, MultipleChoiceTask task,
            Consumer<AnswerData> resultCallback) {
                Spark.unmap("/" + session_id + "/multiChoise/ans");
        post("/" + session_id + "/multiChoise/ans", (spark.Request req, spark.Response res) -> {

            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;

            task.selectedOptions = answer.getAnswer();

            if (answer.getUser().equals(userToken)) {

                eval = task.validate();
                resultCallback.accept(new AnswerData(answer.getUser(), answer.answer, eval));
                return (String.valueOf(eval));
            } else {
                resultCallback.accept(null);
                return "Error: Wrong session";
            }

        });

    }
}
