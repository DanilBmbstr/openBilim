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

public class Router {

    public static void sendNewTask(Task task, String session_id) {
        get("/" + session_id + "/getTask", (req, res) -> task.getTaskText());
    }

    // Для разных типов ответа используем разные эндпоинты
    // Колбэк используется для обработки результата
    public void handleTextAnswer(String session_id, TextTask task, Consumer<AnswerData> resultCallback) {
        post("/"+ session_id + "/textAns/ans", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;
            task.answer = answer.getAnswer();
            eval = task.validate();
            resultCallback.accept(new AnswerData(answer.userToken, answer.answer, eval));
            return (String.valueOf(eval));

        });
    }

    public void handleSingleChoiseAnswer(String session_id, SingleChoiceTask task, Consumer<AnswerData> resultCallback) {
        post("/"+ session_id + "/singleChoise/ans", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;
            task.selectedOption = answer.getAnswer();
            eval = task.validate();
            resultCallback.accept(new AnswerData(answer.userToken, answer.answer, eval));
            return (String.valueOf(eval));

        });
        
    }
    public void handleMultipleChoiseAnswer(String session_id, MultipleChoiceTask task, Consumer<AnswerData> resultCallback) {
        post("/"+ session_id + "/multiChoise/ans", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
            boolean eval;
            task.selectedOptions = answer.getAnswer();
            eval = task.validate();
            resultCallback.accept(new AnswerData(answer.userToken, answer.answer, eval));
            return (String.valueOf(eval));

        });
        
    }
}
