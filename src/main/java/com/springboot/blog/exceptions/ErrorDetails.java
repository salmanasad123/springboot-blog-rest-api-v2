package com.springboot.blog.exceptions;

import java.util.Date;

public class ErrorDetails {

    private Date timeStamp;
    private int statusCode;
    private String details;
    private String message;

    public ErrorDetails(Date timeStamp, int statusCode, String details, String message) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.details = details;
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetails() {
        return details;
    }

    public String getMessage() {
        return message;
    }
}
