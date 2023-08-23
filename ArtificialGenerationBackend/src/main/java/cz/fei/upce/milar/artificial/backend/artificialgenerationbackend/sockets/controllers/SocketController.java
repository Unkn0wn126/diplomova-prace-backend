package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.controllers;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.ActiveEndpointsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.domain.ClientInfo;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.common.services.ActiveClientsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.dto.AfterConnectResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util.SocketEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketController {
    private static final Logger logger = LoggerFactory.getLogger(SocketController.class);
    private final ActiveClientsService activeClientsService;

    private final ActiveEndpointsService activeEndpointsService;
    private final SocketIOServer server;

    public SocketController(SocketIOServer server,
                            ActiveClientsService activeClientsService, ActiveEndpointsService activeEndpointsService) {
        this.server = server;
        this.activeClientsService = activeClientsService;
        this.activeEndpointsService = activeEndpointsService;
        this.server.addConnectListener(onConnected());
        this.server.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            logger.info("Connected");
            activeClientsService.registerClient(client.getSessionId());
            ObjectMapper mapper = new ObjectMapper();
            try {
                String onConnectedResponse = mapper.writeValueAsString(
                        new AfterConnectResponseDto(client.getSessionId().toString(),
                                activeEndpointsService.getApiPort(),
                                activeEndpointsService.composeParseModelPath(),
                                activeEndpointsService.composeGenerateScenesPath()));

                client.sendEvent(SocketEventTypes.AFTER_CONNECT.getValue(), onConnectedResponse);

                logger.info("Client socket id " + client.getSessionId());
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
            }
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            ClientInfo clientInfo = activeClientsService.unregisterClient(client.getSessionId());
            logger.info("Client[{}] - Disconnected from socket", clientInfo.getSocketID().toString());
        };
    }
}
