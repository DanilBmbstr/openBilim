package com.openBilim.HTTP_Handling;

import java.util.List;

public class ChoiseTaskDTO {
    public String taskText;
    public List<String> options;
    public ChoiseTaskDTO(String taskText, List<String> options){
        this.taskText = taskText;
        this.options = options;
    }
}

