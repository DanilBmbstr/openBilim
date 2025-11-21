package com.openBilim.Session_Handling;

public class TestDTO {
    public String subject;
    public String testName;
    public int tasksNumber;
    public String session_id;
    public TestDTO(String subject, String testName, int tasksNumber, String session_id){
        this.subject = subject;
        this.testName = testName;
        this.tasksNumber = tasksNumber;
        this.session_id = session_id;
    }
}
