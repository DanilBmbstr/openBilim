package com.openBilim.Session_Handling;
import java.util.ArrayList;


import com.openBilim.Tasks.*;
public class Test {
    private ArrayList<Task> tasks;
    private String testName;
    private String subject;


    public Test(String subject, String testName){
        this.subject = subject;
        this.testName = testName;
        tasks = new ArrayList<>();
        
    }

    public String getSubject() { return subject; }
    public String getTestName() { return testName; }
    
    public void pushTask(Task task){
        tasks.add(task);
    }



}
