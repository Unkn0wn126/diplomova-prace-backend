package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.controllers;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain.ClientInfo;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util.SocketEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SocketController {
    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);
    /// based on: https://medium.com/folksdev/spring-boot-netty-socket-io-example-3f21fcc1147d
    private final SocketIOServer server;
    private final SocketService socketService;
    private final ActiveClientsService activeClientsService;

    public SocketController(SocketIOServer server, SocketService socketService,
                            ActiveClientsService activeClientsService) {
        this.server = server;
        this.socketService = socketService;
        this.activeClientsService = activeClientsService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            logger.info("Connected");
            activeClientsService.registerClient(client.getSessionId());
            client.sendEvent(SocketEventTypes.AFTER_CONNECT.getValue(), client.getSessionId().toString());

            logger.info("Client socket id " + client.getSessionId());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            ClientInfo clientInfo = activeClientsService.unregisterClient(client.getSessionId());
            logger.info("Client[{}] - Disconnected from socket", clientInfo.getSocketID().toString());
        };
    }
}
