package com.openBilim.HTTP_Handling;


public class AnswerData extends AnswerRequest{


    public AnswerData(String userToken,String answer, boolean validation){
        this.userToken = userToken;
        this.answer = answer;
        this.validation = validation;
    }
        public AnswerData(){

    }

    public void init(String userToken,String answer, boolean validation){
        this.userToken = userToken;
        this.answer = answer;
        this.validation = validation;
    }
    //private String task_Id;


    public boolean validation;

    public String getUser(){return userToken;}
    public String getAnswer(){return answer;}

    public void setUser (String user_id){this.userToken = user_id;}
    public void setAnswer(String answer) {this.answer = answer;}
}
