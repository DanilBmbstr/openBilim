package com.openBilim;
import static spark.Spark.port;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.FileInputStream;
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
import com.openBilim.Users.User;
import com.openBilim.Users.Authorization.*;
import com.openBilim.LOGGER;

class Main{

    

public static void main(String[] args)
{


    
    
//___________________________________Запуск сервера
    port(Integer.parseInt(args[0]));
    try{
    Spark.init();
//___________________________________________________________________________________

//Списки пользователей и тестов
Auth.userList = new ArrayList<>();
List<Test> testList = new ArrayList<>();

Auth.userList.add(new User("Serj", "Сергей Владимирович Коротков", "БПО-29-02", "1@mail.ru", "123", "STUDENT"));
Auth.userList.add(new User("Andrew", "Андрей Вячеславович Дворянцев", "БПО-29-02", "2@mail.ru", "aaa", "STUDENT"));
Auth.userList.add(new User("Ramil", "Кунаев Рамиль Тарасович", "БПО-29-02", "3@mail.ru", "000", "STUDENT"));




Auth.auth();
Auth.validate();

//Создание теста + сессии, запуск сессии с айди 1234. Пример:
//Проверка пользователя пока ещё не добавлена
Test sampleTest = new Test("1","Subj", "Name");

testList.add(sampleTest);

SessionCreator.create(testList);



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


//__________________________________________________________--



LOGGER.info("Сервер успешно запущен на порте " + port());

    





}
    catch(IllegalStateException e) {System.err.println("Failed to initialize server: " + e.getMessage());}
    



    
}

    }