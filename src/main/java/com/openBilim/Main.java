package com.openBilim;
import static spark.Spark.port;



import spark.Spark;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.TextTask;
import com.openBilim.Session_Handling.*;


class Main{

public static void main(String[] args)
{
    
//___________________________________Запуск сервера
    port(Integer.parseInt(args[0]));
    try{
    Spark.init();
//___________________________________________________________________________________

//



//Создание теста + сессии, запуск сессии с айди 1234. Пример:
//Проверка пользователя пока ещё не добавлена
Test sampleTest = new Test("Subj", "Name");
UserSession sampleSession = new UserSession("1234", sampleTest, "Vasya_Pupkin");

TextTask sampleTask = new TextTask("Введите число 1", "1");
sampleTest.pushTask(sampleTask);
sampleSession.run();
//__________________________________________________________--




    System.out.println("Сервер успешно запущен на порте " + port());

    





}
    catch(IllegalStateException e) {System.err.println("Failed to initialize server: " + e.getMessage());}
    



    
}

    }