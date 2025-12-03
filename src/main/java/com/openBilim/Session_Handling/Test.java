package com.openBilim.Session_Handling;
import java.util.ArrayList;


import com.openBilim.Tasks.*;
public class Test {
    private final String test_id;
    private ArrayList<Task> tasks;
    private String testName;
    private String subject;

    

    public Test(String test_id, String subject, String testName){
        this.subject = subject;
        this.testName = testName;
        tasks = new ArrayList<>();
        this.test_id = test_id;
    }

    public double getMaxScore()
    {
        double result = 0;
        for(int i = 0; i < tasks.size(); i++)
        {
            result += tasks.get(i).getPoints();
        }
        return result;
    }

    public String getSubject()
    {
        return subject;
    }

    public void pushTask(Task task){
        tasks.add(task);
    }

    public void setTasksList(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    public String getId()
    {return test_id;}
    public String getName()
    {return testName;}
    public Task getTask(int taskIterator){
        if(tasks.size() > 0 && taskIterator < tasks.size() && taskIterator >= 0){
        Task task = tasks.get(taskIterator);
        return task;
        }
        else return null;
    }

    public int getTasksNumber(){
        return tasks.size();
    }

}
