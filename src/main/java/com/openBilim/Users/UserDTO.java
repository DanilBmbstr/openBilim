package com.openBilim.Users;

public class UserDTO {
    public String fio;
    public String user_id;
    public String group; 

    public UserDTO(String fio, String user_id, String group ){
        this.fio = fio;
        this.user_id = user_id;
        this.group = group;
    }
}
