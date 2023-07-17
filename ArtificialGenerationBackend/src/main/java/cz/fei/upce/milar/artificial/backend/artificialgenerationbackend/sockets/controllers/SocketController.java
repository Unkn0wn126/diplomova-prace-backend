package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.controllers;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util.SocketEventTypes;
import org.springframework.stereotype.Component;

@Component
public class SocketController {
    /// based on: https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketController(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            client.sendEvent(SocketEventTypes.AFTER_CONNECT.getValue(), client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            //log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }
}
