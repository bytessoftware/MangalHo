package com.bytes.mangalho.Models.LoginWithOtp;

import com.google.gson.annotations.SerializedName;

public class GeneratOtp {
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String  message;
    @SerializedName("otp")
    public String  otp;

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
