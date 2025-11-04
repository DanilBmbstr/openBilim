package com.openBilim.HTTP_Handling;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.function.Consumer;

import spark.Spark;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.*;
import com.openBilim.Tasks.Task;
import com.openBilim.Tasks.TextTask;

public class Router {





    public static void sendNewTask(Task task){
    get("/getTask", (req, res) ->  task.getTaskText());
    }


    //Для разных типов ответа используем разные эндпоинты
    //Колбэк используется для обработки результата
    public static void handleTextAnswer(TextTask task, Consumer<AnswerData> resultCallback){
    post("/textAns/ans", (spark.Request req, spark.Response res) -> {
    ObjectMapper objectMapper = new ObjectMapper();
    AnswerRequest answer = objectMapper.readValue(req.body(), AnswerRequest.class);
    boolean eval;
    task.answer = answer.getAnswer();
    eval = task.validate(); 
    resultCallback.accept(new AnswerData(answer.userToken, answer.answer, eval));
    return(String.valueOf(eval));
    
}); 
    }
}
