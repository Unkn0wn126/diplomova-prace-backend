package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.UUID;

public class TaskCreatedResponseDto {
    private final UUID operationID;

    public TaskCreatedResponseDto(UUID operationID) {
        this.operationID = operationID;
    }

    @JsonSetter("operationID")
    public UUID getOperationID() {
        return operationID;
    }
}
