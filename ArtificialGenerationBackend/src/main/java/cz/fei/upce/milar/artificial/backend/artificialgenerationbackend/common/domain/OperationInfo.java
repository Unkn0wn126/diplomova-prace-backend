package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain;

import java.nio.file.Path;
import java.util.UUID;

public class OperationInfo {
    private final UUID operationID;
    private final Path[] resultPaths;

    public OperationInfo(UUID operationID, int resultsSize) {
        this.operationID = operationID;
        resultPaths = new Path[resultsSize];
    }

    public UUID getOperationID() {
        return operationID;
    }

    public Path[] getResultPaths() {
        return resultPaths;
    }
}
