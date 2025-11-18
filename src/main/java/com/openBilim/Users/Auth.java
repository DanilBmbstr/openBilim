package com.openBilim.Users;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.HTTP_Handling.AnswerData;
import com.openBilim.HTTP_Handling.AnswerRequest;
import java.util.List;
public class Auth {

public static List<User> userList;
//По необходимости переделаем под БД

//Полным пилотажем будет если мы ещё будем шифровать пароль и юзер айди(токен)
    public static synchronized void auth(){
        post("/auth", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();

            LoginDTO login = objectMapper.readValue(req.body(), LoginDTO.class);
            UserDTO userDTO;
            for(int i = 0; i < userList.size(); i++){
                if(userList.get(i).getEmail().equals(login.login)){
                    if(userList.get(i).getPassword().equals(login.password)){
                        
                        userDTO = new UserDTO(userList.get(i).getFio(),userList.get(i).getUserId(),userList.get(i).getGroup());
                        return objectMapper.writeValueAsString(userDTO);
                    }
                }
            }

            return "Error: Test not found";

        });
    }
}
