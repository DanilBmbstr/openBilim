package com.openBilim.HTTP_Handling;
import java.util.List;
    import java.util.Arrays;
public class AnswerRequest {
    public String user_token;
    //private String task_Id;
    public String answer;

    public String getUserToken(){return user_token; }
    public String getAnswer(){return answer;}

    public void setUser (String user_id){this.user_token = user_id;}
    public void setAnswer(String answer) {this.answer = answer;}

    public static List<String> parceMultipleChoise(String answer){        
        return Arrays.asList(answer.split(","));
    }
}
