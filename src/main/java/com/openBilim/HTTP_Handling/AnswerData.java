package com.openBilim.HTTP_Handling;


public class AnswerData{

    public String user_id;
    public String answer;

    public AnswerData(String user_id,String answer, boolean validation){
        this.user_id = user_id;
        this.answer = answer;
        this.validation = validation;
    }
        public AnswerData(){

    }

    public void init(String user_id,String answer, boolean validation){
        this.user_id = user_id;
        this.answer = answer;
        this.validation = validation;
    }
    //private String task_Id;


    public boolean validation;


    public String getAnswer(){return answer;}
    public String getUserId(){return user_id;}

    public void setUser (String user_id){this.user_id = user_id;}
    public void setAnswer(String answer) {this.answer = answer;}
}
