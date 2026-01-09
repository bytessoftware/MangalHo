package com.bytes.mangalho.LoginWithOtp;

import com.bytes.mangalho.Models.LoginWithOtp.Data;
import com.google.gson.annotations.SerializedName;

public class RegisterRes {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
