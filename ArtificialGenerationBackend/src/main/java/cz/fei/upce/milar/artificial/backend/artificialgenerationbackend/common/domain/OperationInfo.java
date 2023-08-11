package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OperationInfo {
    private UUID operationID;
    private List<Path> resultPaths;

    public OperationInfo(UUID operationID) {
        this.operationID = operationID;
        resultPaths = new ArrayList<>();
    }

    public UUID getOperationID() {
        return operationID;
    }

    public List<Path> getResultPaths() {
        return resultPaths;
    }
}
