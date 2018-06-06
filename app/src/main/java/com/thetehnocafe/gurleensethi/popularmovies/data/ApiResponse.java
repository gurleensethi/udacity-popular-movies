package com.thetehnocafe.gurleensethi.popularmovies.data;

public class ApiResponse<T> {
    private boolean isSuccessful;
    private T data;
    private String message;

    public ApiResponse(boolean isSuccessful, T data, String message) {
        this.isSuccessful = isSuccessful;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
