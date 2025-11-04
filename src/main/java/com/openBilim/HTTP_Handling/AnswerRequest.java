package com.openBilim.HTTP_Handling;


public class AnswerRequest {
    public String userToken;
    //private String task_Id;
    public String answer;

    public String getUser(){return userToken;}
    public String getAnswer(){return answer;}

    public void setUser (String user_id){this.userToken = user_id;}
    public void setAnswer(String answer) {this.answer = answer;}
}
