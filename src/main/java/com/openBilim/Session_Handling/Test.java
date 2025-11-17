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

    
    public void pushTask(Task task){
        tasks.add(task);
    }

    public Task getTask(int taskIterator){
        if(tasks.size() > 0 && taskIterator < tasks.size() && taskIterator >= 0){
        Task task = tasks.get(taskIterator);
        return task;
        }
        else return null;
    }


}
