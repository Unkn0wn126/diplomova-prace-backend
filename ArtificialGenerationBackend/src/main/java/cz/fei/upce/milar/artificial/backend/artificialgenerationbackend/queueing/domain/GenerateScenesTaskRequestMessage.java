package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GenerateSingleSceneCommandDto;

import java.util.UUID;

public class GenerateScenesTaskRequestMessage {
    private UUID clientID;
    private UUID operationID;
    private int operationIndex;
    private OperationType operationType;
    private GenerateSingleSceneCommandDto content;

    public UUID getClientID() {
        return clientID;
    }

    public void setClientID(UUID clientID) {
        this.clientID = clientID;
    }

    public UUID getOperationID() {
        return operationID;
    }

    public void setOperationID(UUID operationID) {
        this.operationID = operationID;
    }

    public int getOperationIndex() {
        return operationIndex;
    }

    public void setOperationIndex(int operationIndex) {
        this.operationIndex = operationIndex;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public GenerateSingleSceneCommandDto getContent() {
        return content;
    }

    public void setContent(GenerateSingleSceneCommandDto content) {
        this.content = content;
    }
}
