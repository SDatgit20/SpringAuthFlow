package com.example.demo.file;

	@SuppressWarnings("serial")
	public class FileStorageException extends RuntimeException {
	    public FileStorageException(String message) {
	        super(message);
	    }

	    public FileStorageException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}