package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientInfo {
    private final UUID socketID;
    private final Map<UUID, OperationInfo> operationsMap;

    public ClientInfo(UUID socketID) {
        this.socketID = socketID;
        operationsMap = new ConcurrentHashMap<>();
    }

    public UUID getSocketID() {
        return socketID;
    }

    public void addOperationToClient(UUID operationID, int resultsSize) {
        if (operationsMap.containsKey(operationID)) {
            throw new RuntimeException("Operation already registered for client");
        }

        if (resultsSize < 1) {
            throw new IllegalArgumentException("Must expect at least one result");
        }

        operationsMap.put(operationID, new OperationInfo(operationID, resultsSize));
    }

    public OperationInfo removeOperationFromClient(UUID operationID) {
        if (!operationsMap.containsKey(operationID)) {
            throw new RuntimeException("No such operation for client");
        }

        return operationsMap.remove(operationID);
    }

    public void addResultFilePathToOperation(UUID operationID, Path path, int index) {
        if (!operationsMap.containsKey(operationID)) {
            throw new RuntimeException("No such operation for client");
        }

        Path[] resultPaths = operationsMap.get(operationID).getResultPaths();

        if (index > resultPaths.length || index < 0) {
            throw new IllegalArgumentException("Invalid index");
        }

        resultPaths[index] = path;
    }

    public Path[] getResultFilePathsOfOperation(UUID operationID) {
        if (!operationsMap.containsKey(operationID)) {
            throw new RuntimeException("No such operation for client");
        }

        return operationsMap.get(operationID).getResultPaths();
    }
}
