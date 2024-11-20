package com.example.demo.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Component
@ConfigurationProperties(prefix="file")
public class props {
	private String updir;

	public String getUpdir() {
		return updir;
	}

	public void setUpdir(String updir) {
		this.updir = updir;
	}
}
