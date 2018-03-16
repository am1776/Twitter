package com.amallya.twittermvvm.models;

/**
 * Created by anmallya on 3/15/2018.
 */

public class Response<T> {

    public static final String GENERIC_SUCCESS_MSG = "Success";

    private T data;
    private String message;
    private Status responseCode;

    public Response(String message, Status errorCode){
        this.message = message;
        this.responseCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Status getErrorCode() {
        return responseCode;
    }

    public void setData(T data){
        this.data = data;
    }

    public enum Status {
        SUCCESS(1, "Success"),
        ERROR(2, "Error"),
        LOADING(3, "Loading");

        private final int id;
        private final String message;
        Status(int id, String message) {
            this.id = id;
            this.message = message;
        }

        public int getId() { return id; }
        public String getMessage() { return message; }
    }
}
