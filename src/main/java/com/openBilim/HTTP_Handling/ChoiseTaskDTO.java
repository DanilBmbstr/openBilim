package com.openBilim.HTTP_Handling;

import java.util.List;

public class ChoiseTaskDTO {
    public String type; 
    public String taskText;
    public List<String> options;
    public ChoiseTaskDTO(String type, String taskText, List<String> options){
        this.type = type;
        this.taskText = taskText;
        this.options = options;
    }
}

