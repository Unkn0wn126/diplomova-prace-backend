package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.util;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OperationResult;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OperationType;

import java.nio.charset.Charset;
import java.util.UUID;

public class ReceivedMessageHelper {
    public final OperationType OPERATION_TYPE;
    public final UUID OPERATION_ID;
    public final UUID CLIENT_ID;
    public final OperationResult RESULT;
    public final byte[] DATA;

    public ReceivedMessageHelper(OperationType operationType, UUID operationId, UUID clientId,
                                 OperationResult operationResult, byte[] data) {
        this.OPERATION_TYPE = operationType;
        this.OPERATION_ID = operationId;
        this.CLIENT_ID = clientId;
        this.RESULT = operationResult;
        this.DATA = data;
    }

    public String dataAsString(Charset charset) {
        return new String(DATA, charset);
    }
}
