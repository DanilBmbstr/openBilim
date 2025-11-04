package com.openBilim.Tasks;


//Класс котоырй репрезентует задания, для которых требуется самостоятельный ввод правильного ответа в текстовое поле
public class TextTask extends Task {
    private String taskText;
    
    private String rightAnswer;

    public String answer;

    public TextTask(String taskText, String rightAnswer){
        super(taskText);
        this.rightAnswer = rightAnswer;
    }

    public boolean validate(){
        return answer.equals(rightAnswer);
    };


  

}
