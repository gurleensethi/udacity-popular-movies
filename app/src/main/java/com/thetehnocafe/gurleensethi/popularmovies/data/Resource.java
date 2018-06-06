package com.thetehnocafe.gurleensethi.popularmovies.data;

@SuppressWarnings({"unchecked", "WeakerAccess"})
public class Resource<T> {
    public enum Status {
        SUCCESS,
        LOADING,
        ERROR
    }

    private Status status;
    private T data;
    private String message;

    public Resource(T data, Status status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public static <T> Resource success(T data) {
        return new Resource(data, Status.SUCCESS, null);
    }

    public static <T> Resource loading(T data) {
        return new Resource(data, Status.LOADING, null);
    }

    public static <T> Resource error(String message, T data) {
        return new Resource(data, Status.ERROR, message);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
