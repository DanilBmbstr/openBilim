package com.openBilim.Users;



import com.openBilim.Users.Authorization.Hash;;

public class User {
    private final String user_id;
    private final String fio;
    private final String email;
    private final String role; //TEACHER //STUDENT
    private final String group;
    private final String password_salt;
    private final String password_hash;
    //private int correctAnswersCount = 0;
    // Я не знаю зачем это поле было добавлено, но на всякий удалять не буду

    public User(String user_id, String fio, String group, String email, String password, String role){
        this.user_id = user_id;
        this.fio = fio;
        this.email =email;
        this.role = role;
        this.password_salt= Hash.generateSalt();
        this.password_hash = Hash.hashString((Hash.hashString(password, "SHA-256")) + password_salt, "SHA-256");
        this.group = group;
    }
    // Геттеры
    public String getUserId(){return user_id; }
    public String getFio(){return fio; }
    public String getEmail(){return email; }
    public String getRole(){return role; }
    
    public String getPasswordSalt(){
        return password_salt;
    }
    public String getPasswordHash(){
        return password_hash;
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
