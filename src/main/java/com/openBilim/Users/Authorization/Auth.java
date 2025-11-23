package com.openBilim.Users.Authorization;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.HTTP_Handling.AnswerData;
import com.openBilim.HTTP_Handling.AnswerRequest;
import com.openBilim.Users.User;
import com.openBilim.Users.UserDTO;
import com.openBilim.Users.Authorization.JWT_Util;
import com.openBilim.Users.Authorization.Hash;

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
                    if(userList.get(i).getPasswordHash().equals( Hash.hashString(Hash.hashString(login.password, "SHA-256") + userList.get(i).getPasswordSalt(), "SHA-256"))){
                        
                        userDTO = new UserDTO(userList.get(i).getFio(),userList.get(i).getUserId(),userList.get(i).getGroup());
                        //return objectMapper.writeValueAsString(userDTO);
                        return JWT_Util.createTokenWithClaims(userList.get(i).getEmail(),userList.get(i).getRole(),userList.get(i).getFio(),  userList.get(i).getUserId());
                    }
                }
            }

            return "Error: User not found";

        });
    }


    public static synchronized void validate(){
        post("/validate", (spark.Request req, spark.Response res) -> {
            return     JWT_Util.validate(req.body())
;

        });
    }
}
