package com.openBilim.Users;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final String user_id;
    private final String fio;
    private final Steing email;
    private final String role; //TEACHER //STUDENT
    private int correctAnswersCount = 0;
    public User(String user_ud, String fio, String email, String role){
        this.user_id = user_id;
        this.fio = fio;
        this.email =email;
        this.role = role;
    }
    // Геттеры
    public String getUserId(){return user_ud; }
    public String getFio(){return fio; }
    public String getEmail(){return email; }
    public String getRole(){return role; }
    
    public int getCorrectAnswerCount(){return correctAnswersCount;}
    public boolean canCreateTests() {
        return role == "TEACHER";
    }

    public boolean canViewAnalytics() {
        return role == "TEACHER";
    }
    public void completeTest(int correctAnswers, int totalQuestions) {
        this.correctAnswersCount += correctAnswers;
        
    }
}
