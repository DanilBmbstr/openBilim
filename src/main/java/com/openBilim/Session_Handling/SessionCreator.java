package com.openBilim.Session_Handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.Users.UserDTO;
import com.openBilim.Users.Authorization.Auth;
import com.openBilim.Users.Authorization.JWT_Util;
import com.openBilim.Users.Authorization.LoginDTO;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class SessionCreator {

    public static List<UserSession> sessionList;

    public static void create(List<Test> testList) {
        if (sessionList == null) {
            sessionList = new ArrayList<>();
        }
        post("/startTest", (spark.Request req, spark.Response res) -> {

            ObjectMapper objectMapper = new ObjectMapper();

            SessionDTO dto = objectMapper.readValue(req.body(), SessionDTO.class);

            for (int i = 0; i < Auth.userList.size(); i++) {
                if (JWT_Util.validateAndGetUserId(dto.user_token).equals(Auth.userList.get(i).getUserId())) {
                    break;
                }
                if (i == Auth.userList.size()) {
                    return "Error! User does not exist";
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
