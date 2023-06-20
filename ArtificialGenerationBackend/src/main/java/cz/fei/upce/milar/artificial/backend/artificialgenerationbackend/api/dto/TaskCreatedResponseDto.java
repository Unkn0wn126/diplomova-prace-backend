package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

import java.util.UUID;

public class TaskCreatedResponseDto extends DtoAncestor {
    private UUID operationID;

    public UUID getOperationID() {
        return operationID;
    }

    public void setOperationID(UUID operationID) {
        this.operationID = operationID;
    }
}
