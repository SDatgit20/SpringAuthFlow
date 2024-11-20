package com.example.demo.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;


@Data
@Entity
public class loginm {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
long id;

private String name;

private String status;

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
@Column(unique=true)
@Email(message="enter valid email")
private String email;

String pass;
//@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z][A-Z]).{8,32}$",message="Minimum eight characters, at least one letter and one number")

String cpass;

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
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
@Override
public String toString() {
	return "loginm [id=" + id + ", name=" + name + ", status=" + status + ", email="
			+ email + ", pass=" + pass + ", cpass=" + cpass + "]";
}


}
