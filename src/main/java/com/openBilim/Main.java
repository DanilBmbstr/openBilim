package com.openBilim;
import static spark.Spark.port;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import spark.Spark;

import com.openBilim.HTTP_Handling.*;
import com.openBilim.Tasks.MultipleChoiceTask;
import com.openBilim.Tasks.SingleChoiceTask;
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
UserSession sampleSession = new UserSession("1234", sampleTest, "Serega");
UserSession sampleSession2 = new UserSession("2345", sampleTest, "Andrew");
UserSession sampleSession3 = new UserSession("3456", sampleTest, "Ramil");

sampleTest.pushTask(new TextTask("Введите число 1", "1", 0.5));
List<String> sampleOptions = new ArrayList<String>();
sampleOptions.add("a");
sampleOptions.add("b");
sampleOptions.add("c");
sampleTest.pushTask(new SingleChoiceTask("Выбери вариант b", sampleOptions ,1, 1.0));

Set<Integer> rightOptions = new HashSet<Integer>();
rightOptions.add(0);
rightOptions.add(1);

sampleTest.pushTask(new MultipleChoiceTask("Выбери варианты a, b", sampleOptions ,rightOptions, 2.0));
sampleSession.run();
sampleSession2.run();
sampleSession3.run();

//__________________________________________________________--




    System.out.println("Сервер успешно запущен на порте " + port());

    





}
    catch(IllegalStateException e) {System.err.println("Failed to initialize server: " + e.getMessage());}
    



    
}

    }