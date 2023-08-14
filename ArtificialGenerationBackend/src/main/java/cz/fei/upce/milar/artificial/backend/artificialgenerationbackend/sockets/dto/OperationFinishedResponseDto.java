package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.UUID;

public class OperationFinishedResponseDto {
    UUID operationID;
    boolean success;
    String message;
    String resultUrl;

    public OperationFinishedResponseDto(UUID operationID, boolean success, String message, String resultUrl) {
        this.operationID = operationID;
        this.success = success;
        this.message = message;
        this.resultUrl = resultUrl;
    }

    @JsonGetter("operationID")
    public UUID getOperationID() {
        return operationID;
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
