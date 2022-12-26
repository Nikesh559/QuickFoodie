package com.foodie.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
public class Message {
    private Timestamp timestamp;
    private HttpStatus status;
    private String msg = "";
    private String error = "";


    public Message(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public void setStatus(HttpStatus status) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
    }

    public void setMsg(String msg) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.msg = msg;
    }
}