package com.openBilim.Session_Handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openBilim.Users.Auth;
import com.openBilim.Users.LoginDTO;
import com.openBilim.Users.UserDTO;

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
                if (dto.user_token.equals(Auth.userList.get(i).getUserId())) {

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
                        token = String.valueOf(Integer.parseInt(sessionList.getLast().getToken()) + 1);
                    }
                    sessionList.add(new UserSession(token, testList.get(i), dto.user_token));
                    sessionList.getLast().processTask();
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
