package com.bytes.mangalho.Models.LoginWithOtp;

import com.bytes.mangalho.Models.LoginDTO;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable {

	@SerializedName("data")
	private LoginDTO data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(LoginDTO data){
		this.data = data;
	}

	public LoginDTO getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}
}