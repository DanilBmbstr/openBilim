package com.openBilim.Session_Handling;

public class TestDTO {
    public String subject;
    public String testName;
    public int tasksNumber;
    public String sessionToken;
    public TestDTO(String subject, String testName, int tasksNumber, String sessionToken){
        this.subject = subject;
        this.testName = testName;
        this.tasksNumber = tasksNumber;
        this.sessionToken = sessionToken;
    }
}
