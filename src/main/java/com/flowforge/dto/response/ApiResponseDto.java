package com.flowforge.dto.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiResponseDto<T> implements Serializable {
    private Boolean success;
    private Integer code;
    private HttpStatus status;
    private String message;
    private T data = (T) new ArrayList<>();
    private Map<String, Object> meta = new HashMap<>();

    public Map<String, Object> getMeta() {
        return meta;
    }

    public ApiResponseDto addMeta(String key, Object value) {
        meta.put(key, value);
        return this;
    }

    public ApiResponseDto(Boolean success, Integer code, T data) {
        this.success = success;
        this.code = code;
        this.data = data;
    }

    public ApiResponseDto(Boolean success, Integer code, HttpStatus status, T data) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public ApiResponseDto(Boolean success, Integer code, HttpStatus status, String message) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponseDto(Boolean success, Integer code, HttpStatus status, String message, T data) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public ApiResponseDto() {
    }

    public ApiResponseDto(Boolean success, Integer code, HttpStatus status, String message, T data, Map<String, Object> meta) {
        this.success = success;
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
        this.meta = meta;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
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

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "ApiResponseDto{" +
                "success=" + success +
                ", code=" + code +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", meta=" + meta +
                '}';
    }
}
