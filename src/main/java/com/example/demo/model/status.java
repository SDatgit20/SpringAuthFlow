package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class status {
@Id
private long id;
private Date date;
private Timestamp TimeStamp;
private String status;
private String activeStatus;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public Timestamp getTimeStamp() {
	return TimeStamp;
}
public void setTimeStamp(Timestamp timeStamp) {
	TimeStamp = timeStamp;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getActiveStatus() {
	return activeStatus;
}
public void setActiveStatus(String activeStatus) {
	this.activeStatus = activeStatus;
}

}
