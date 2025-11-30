package com.openBilim.Session_Handling;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.openBilim.Users.Authorization.Auth;
import com.openBilim.Users.Authorization.JWT_Util;


import java.util.ArrayList;
import java.util.List;


import static spark.Spark.post;

public class SessionCreator {

    public static List<UserSession> sessionList;

    public static synchronized UserSession getExistingSession(String id){
        for(int i = 0; i < sessionList.size(); i++)
        {
            if(sessionList.get(i).get_id().equals(id)){return sessionList.get(i); }
        }
        return null;
    }


    public static synchronized void create(List<Test> testList) {
        if (sessionList == null) {
            sessionList = new ArrayList<>();
        }
        post("/startTest", (spark.Request req, spark.Response res) -> {

            ObjectMapper objectMapper = new ObjectMapper();

            SessionDTO dto = objectMapper.readValue(req.body(), SessionDTO.class);
            String userId = JWT_Util.validateAndGetUserId(dto.user_token); 
            for (int i = 0; i < Auth.userList.size(); i++) {
                if (userId.equals(Auth.userList.get(i).getUserId())) {
                    break;
                }
                if (i == Auth.userList.size()) {
                    return "Error! User does not exist";
                }
            }

            for(int i = 0; i < sessionList.size(); i++)
            {
                if(userId.equals(sessionList.get(i).getUserId())){
                if(!sessionList.get(i).isFinished())
                return("Error: This user has unfinished session");
                }
            }

            for (int i = 0; i < testList.size(); i++) {
                if (testList.get(i).getId().equals(dto.test_id)) {
                    String token;
                    if (sessionList.size() == 0) {
                        token = "0";
                    } else {
                        token = String.valueOf(Integer.parseInt(sessionList.getLast().get_id()) + 1);
                    }
                    sessionList.add(new UserSession(token, testList.get(i), JWT_Util.validateAndGetUserId(dto.user_token) ));
                    sessionList.getLast().run();
                    ;

                    TestDTO testDTO = new TestDTO(testList.get(i).getSubject(), testList.get(i).getName(),
                            testList.get(i).getTasksNumber(), token);




                            
                    return objectMapper.writeValueAsString(testDTO);
                }
            }

            return "Error: Wrong login or password";

        });


        
    }
}
