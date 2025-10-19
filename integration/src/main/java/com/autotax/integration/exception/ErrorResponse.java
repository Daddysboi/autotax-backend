package com.autotax.integration.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.autotax.integration.apiclient.ApiResponse;
import org.springframework.http.HttpStatus;

/**
 * @author Temitope Yusf <temitopeahmedyusuf@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorResponse extends RuntimeException {
    private final ApiResponse<?> apiResponse;

    public ErrorResponse(ApiResponse<?> apiResponse) {
        super(apiResponse == null ? null : apiResponse.getMessage());
        this.apiResponse = apiResponse;
    }
    public ErrorResponse(int code, String message, Object data) {
        super(message);
        ApiResponse<Object> apiResponse = new ApiResponse<Object>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        this.apiResponse = apiResponse;
    }
    public ErrorResponse(int code, String message) {
        super(message);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        this.apiResponse = apiResponse;
    }

    public ErrorResponse(HttpStatus status, String message) {
        super(message);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(status.value());
        apiResponse.setMessage(message);
        this.apiResponse = apiResponse;
    }

    public ErrorResponse(int code) {
        super(code + "");
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(code);
        this.apiResponse = apiResponse;
    }
    public ErrorResponse(String message) {
        super(message);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(400);
        apiResponse.setMessage(message);
        this.apiResponse = apiResponse;
    }

    public ApiResponse<?> getApiResponse() {
        return apiResponse;
    }
}
