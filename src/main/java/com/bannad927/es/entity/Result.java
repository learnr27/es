package com.bannad927.es.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


public class Result<T> implements Serializable{
	private static final long serialVersionUID = -1L;
	private static final Integer SUCCESS_CODE = 1;
	private Integer code = 1;
	private String message;
	private T data;

	@JsonIgnore
	public static <T> Result getSuccessResult(T content,String msg){
		Result<T> result = new Result<>();
		result.setCode(1);
		result.setMessage(msg);
		result.setData(content);
		return result;
	}

	@JsonIgnore
	public static Result getSuccessResult(){
		Result result = new Result();
		result.setCode(1);
		result.setMessage("success");
		return result;
	}
	@JsonIgnore
	public  static <T> Result getSuccessResult(T content){
		Result<T> result = new Result<>();
		result.setCode(1);
		result.setMessage("success");
		result.setData(content);
		return result;
	}
	@JsonIgnore
	public static Result getFailResult(String message){
		Result result = new Result();
		result.setCode(0);
		result.setMessage(message);
		return result;
	}
	@JsonIgnore
	public static  Result getFailResult(Integer code,String message){
		Result result = new Result();
		result.setCode(code);
		result.setMessage(message);
		return result;
	}
	@JsonIgnore
	public static <T> Result getFailResult(Integer code,String message,T content){
		Result<T> result = new Result<>();
		result.setCode(code);
		result.setMessage(message);
		result.setData(content);
		return result;
	}


	public boolean isSuccess(){
		return SUCCESS_CODE.equals(code);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Result() {
	}

	@Override
	public String toString() {
		return "Result{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
