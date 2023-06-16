package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.dto.DtoAncestor;

import java.io.Serializable;
import java.util.UUID;

public class TaskRequestMessage implements Serializable {
    private UUID clientID;
    private UUID operationID;
    private OPERATION_TYPE operationType;
    private DtoAncestor content;
}
