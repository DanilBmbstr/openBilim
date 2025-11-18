package com.openBilim.Users;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final String user_id;
    private final String fio;
    private final String email;
    private final String role; //TEACHER //STUDENT
    private final String group;
    private final String password;
    //private int correctAnswersCount = 0;
    // Я не знаю зачем это поле было добавлено, но на всякий удалять не буду

    public User(String user_id, String fio, String group, String email, String password, String role){
        this.user_id = user_id;
        this.fio = fio;
        this.email =email;
        this.role = role;
        this.password = password;
        this.group = group;
    }
    // Геттеры
    public String getUserId(){return user_id; }
    public String getFio(){return fio; }
    public String getEmail(){return email; }
    public String getRole(){return role; }
    

    public String getPassword(){
        return password;
    }

    public String getGroup(){
        return group;
    }

   // public int getCorrectAnswerCount(){return correctAnswersCount;}
    public boolean canCreateTests() {
    return "TEACHER".equals(role);
    }

    public boolean canViewAnalytics() {
    return "TEACHER".equals(role);
    }


    /*
    public void completeTest(int correctAnswers, int totalQuestions) {
        this.correctAnswersCount += correctAnswers;
        
    }
         */
}
