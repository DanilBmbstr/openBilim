package com.openBilim;
import static spark.Spark.port;



import spark.Spark;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.TextTask;



class Main{

public static void main(String[] args)
{
    
//___________________________________Запуск сервера
    port(Integer.parseInt(args[0]));
    try{
    Spark.init();
//___________________________________________________________________________________


//Вопросы и ответы тут. Пример:
    TextTask sampleTask = new TextTask("Введите число 1", "1");
    Router.sendNewTask(sampleTask);
    Router.handleTextAnswer(sampleTask, result -> 
    {System.out.println("Пользователь " + result.userToken + " ответил на вопрос \n правильность: " + result.validation);});
//__________________________________________________________--




    System.out.println("Сервер успешно запущен на порте " + port());

    





}
    catch(IllegalStateException e) {System.err.println("Failed to initialize server: " + e.getMessage());}
    



    
}

    }