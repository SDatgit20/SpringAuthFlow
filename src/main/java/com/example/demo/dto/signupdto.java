package com.example.demo.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class signupdto {
private String name;
private String email;
private String pass;
private String cpass;
private String status;

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPass() {
	return pass;
}
public void setPass(String pass) {
	this.pass = pass;
}
public String getCpass() {
	return cpass;
}
public void setCpass(String cpass) {
	this.cpass = cpass;
}
}
