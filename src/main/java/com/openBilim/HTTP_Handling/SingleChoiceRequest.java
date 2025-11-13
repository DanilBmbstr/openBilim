  // src/main/java/com/openBilim/HTTP_Handling/SingleChoiceRequest.java
package com.openBilim.HTTP_Handling;

public class SingleChoiceRequest {
    public String userToken;
    public String selectedOption;   // например "Москва"

    // геттеры/сеттеры не обязательны — Jackson справится и без них
}
