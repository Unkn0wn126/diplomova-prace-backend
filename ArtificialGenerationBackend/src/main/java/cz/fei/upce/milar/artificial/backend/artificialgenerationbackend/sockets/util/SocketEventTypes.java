package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util;

public enum SocketEventTypes {
    AFTER_CONNECT("after_connect"),
    OPERATION_FINISHED("operation_finished"),
    BACKEND_LOG("backend_log");

    private final String value;

    public String getValue() {
        return value;
    }

    SocketEventTypes(String value) {
        this.value = value;
    }
}
