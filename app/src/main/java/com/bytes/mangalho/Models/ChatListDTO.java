package com.bytes.mangalho.Models;

import java.io.Serializable;

public class ChatListDTO implements Serializable {

    String message_head_id = "";
    String updated_at = "";
    String message = "";
    String user_name = "";
    String user_id = "";
    String user_image = "";

    public String getMessage_head_id() {
        return message_head_id;
    }

    public void setMessage_head_id(String message_head_id) {
        this.message_head_id = message_head_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
