package com.backend.api.invoice_manager.responses;

public class ApiResponse<T> {

    private String message;
    private T data;
    private Integer statusCode;
    private Boolean success;

    public ApiResponse(String message, T data, Integer statusCode, Boolean success) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.success = success;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
