package com.bytes.mangalho.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionResponse implements Serializable {

	@SerializedName("data")
	private SubscriptionCheckDto data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private int status;

	public void setData(SubscriptionCheckDto data){
		this.data = data;
	}

	public SubscriptionCheckDto getData(){
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