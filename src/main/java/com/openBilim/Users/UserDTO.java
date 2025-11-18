package com.openBilim.Users;

public class UserDTO {
    public String fio;
    public String user_token;
    public String group; 

    public UserDTO(String fio, String user_token, String group ){
        this.fio = fio;
        this.user_token = user_token;
        this.group = group;
    }
}
