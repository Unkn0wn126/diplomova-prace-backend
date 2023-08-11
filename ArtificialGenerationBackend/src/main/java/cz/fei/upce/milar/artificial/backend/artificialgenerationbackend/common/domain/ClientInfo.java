package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientInfo {
    private UUID socketID;
    private Map<UUID, OperationInfo> operationsMap;

    public ClientInfo(UUID socketID) {
        this.socketID = socketID;
        operationsMap = new HashMap<>();
    }

    public UUID getSocketID() {
        return socketID;
    }

    public void addOperationToClient(UUID operationID) {
        if (operationsMap.containsKey(operationID)) {
            throw new RuntimeException("Operation already registered for client");
        }

        operationsMap.put(operationID, new OperationInfo(operationID));
    }

    public void removeOperationFromClient(UUID operationID) {
        if (!operationsMap.containsKey(operationID)) {
            throw new RuntimeException("No such operation for client");
        }

        operationsMap.remove(operationID);
    }

    public void addResultFilePathToOperation(UUID operationID, Path path) {
        if (!operationsMap.containsKey(operationID)) {
            throw new RuntimeException("No such operation for client");
        }

        operationsMap.get(operationID).getResultPaths().add(path);
    }

    public List<Path> getResultFilePathsOfOperation(UUID operationID) {
        if (!operationsMap.containsKey(operationID)) {
            throw new RuntimeException("No such operation for client");
        }

        return operationsMap.get(operationID).getResultPaths();
    }
}
