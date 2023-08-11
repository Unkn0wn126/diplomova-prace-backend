package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

import java.util.UUID;

public class TaskCreatedResponseDto extends DtoAncestor {
    private final UUID operationID;

    public TaskCreatedResponseDto(UUID operationID) {
        this.operationID = operationID;
    }

    @JsonSetter("operationID")
    public UUID getOperationID() {
        return operationID;
    }
}
