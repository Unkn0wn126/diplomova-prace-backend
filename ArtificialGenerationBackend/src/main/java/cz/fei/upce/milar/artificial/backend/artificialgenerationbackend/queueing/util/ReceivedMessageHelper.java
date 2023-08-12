package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.util;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_RESULT;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_TYPE;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ReceivedMessageHelper {
    public final OPERATION_TYPE OPERATION_TYPE;
    public final UUID OPERATION_ID;
    public final UUID CLIENT_ID;
    public final OPERATION_RESULT RESULT;
    public final byte[] DATA;

    public ReceivedMessageHelper(OPERATION_TYPE operationType, UUID operationId, UUID clientId,
                                 OPERATION_RESULT operationResult, byte[] data) {
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
