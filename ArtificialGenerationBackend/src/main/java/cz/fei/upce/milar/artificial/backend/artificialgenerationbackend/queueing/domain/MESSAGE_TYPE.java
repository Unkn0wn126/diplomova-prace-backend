package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain;

import java.io.Serializable;

public enum MESSAGE_TYPE implements Serializable {
    REQUEST,
    RESULT,
    LOG
}
