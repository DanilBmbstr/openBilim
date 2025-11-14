package com.openBilim.Session_Handling;
import java.util.ArrayList;


import com.openBilim.Tasks.*;
public class Test {
    private ArrayList<Task> tasks;
    private int taskIterator;
    private String testName;
    private String subject;


    public Test(String subject, String testName){
        this.subject = subject;
        this.testName = testName;
        tasks = new ArrayList<>();
        taskIterator = 0;
    }

    public int getCurrentTask(){
        return taskIterator;
    }
    public void pushTask(Task task){
        tasks.add(task);
    }

    public Task popTask(){
        if(tasks.size() > 0 && taskIterator < tasks.size()){
        Task task = tasks.get(taskIterator);
        taskIterator++;
        return task;
        }
        else return null;
    }


}
