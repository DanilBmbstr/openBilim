package com.openBilim;
import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.options;
import static spark.Spark.port;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import spark.Spark;
import spark.Filter;

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
    DB_Utul.db_adress= "jdbc:postgresql://localhost:5432/openBilim";
    DB_Utul.connect();



//___________________________________Запуск сервера
    port(Integer.parseInt(args[0]));
    //Spark.ipAdress("0.0.0.0");
    try{

    Spark.init();

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
//___________________________________________________________________________________

//Списки пользователей и тестов
//Auth.userList = new ArrayList<>();

/*
Auth.userList.add(new User("Serj", "Сергей Владимирович Коротков", "БПО-29-02", "1@mail.ru", "123", "STUDENT"));
Auth.userList.add(new User("Andrew", "Андрей Вячеславович Дворянцев", "БПО-29-02", "2@mail.ru", "aaa", "STUDENT"));
Auth.userList.add(new User("Ramil", "Кунаев Рамиль Тарасович", "БПО-29-02", "3@mail.ru", "000", "STUDENT"));
 */



Auth.auth();
Auth.validate();
Auth.validateAndGetJson();

Router.getAvailableTests();
Router.getUnfinishedTests();
//Создание теста + сессии, запуск сессии с айди 1234. Пример:
//Проверка пользователя пока ещё не добавлена



SessionCreator.create(DB_Utul.getAvailableTests("0"));





//__________________________________________________________--



LOGGER.info("Сервер успешно запущен на порте " + port());

    





}
    catch(IllegalStateException e) {System.err.println("Failed to initialize server: " + e.getMessage());}
    



    
}

    }