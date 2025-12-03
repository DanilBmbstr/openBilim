package com.openBilim.Users.Authorization;


import static spark.Spark.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.DB_Utul;
import com.openBilim.Users.User;



import java.util.List;
public class Auth {


//Полным пилотажем будет если мы ещё будем шифровать пароль и юзер айди(токен)
    public static synchronized void auth(){
        
        post("/auth", (spark.Request req, spark.Response res) -> {
            ObjectMapper objectMapper = new ObjectMapper();

            LoginDTO login = objectMapper.readValue(req.body(), LoginDTO.class);


            User newUser = DB_Utul.getUserByEmail(login.login);
            if(newUser.getPasswordHash().equals( Hash.hashString(newUser.getPasswordSalt() + login.password, "SHA-256")))
            {
                return JWT_Util.createTokenWithClaims(newUser.getEmail(),newUser.getRole(),newUser.getFio(),newUser.getGroup(),  newUser.getUserId());
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


        public static synchronized void validateAndGetJson(){
        post("/getUserData", (spark.Request req, spark.Response res) -> {
            return     JWT_Util.getJsonUserData(req.body())
;

        });
    }


    
}
