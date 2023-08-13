package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

public class OperationFinishedResponseDto {
    boolean success;
    String message;
    String resultUrl;

    public OperationFinishedResponseDto(boolean success, String message, String resultUrl) {
        this.success = success;
        this.message = message;
        this.resultUrl = resultUrl;
    }

    @JsonGetter("isSuccess")
    public boolean isSuccess() {
        return success;
    }

    @JsonGetter("message")
    public String getMessage() {
        return message;
    }

    @JsonGetter("resultUrl")
    public String getResultUrl() {
        return resultUrl;
    }
}
