package com.openBilim.HTTP_Handling;

public class TestResultDTO {
    public String type = "result";
    public String score;
    public String maxScore;
    public TestResultDTO(double score, double maxScore){
        this.score = String.valueOf(score);
        this.maxScore = String.valueOf(maxScore);
    }   
}
