package com.blogmanage.blogmanagement.exception;

public class InvalidBlogException extends RuntimeException {
    public InvalidBlogException(String message) {
        super(message);
    }
}