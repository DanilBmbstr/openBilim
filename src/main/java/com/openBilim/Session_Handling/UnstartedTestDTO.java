package com.openBilim.Session_Handling;

public class UnstartedTestDTO {
    public String subject;
    public String testName;
    public int tasksNumber;
    public String test_id;
    public UnstartedTestDTO(String subject, String testName, int tasksNumber, String test_id){
        this.subject = subject;
        this.testName = testName;
        this.tasksNumber = tasksNumber;
        this.test_id = test_id;
    }
}
