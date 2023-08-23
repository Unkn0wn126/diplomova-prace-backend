package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.dto.OperationFinishedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util.SocketEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SocketService {
    private final Logger logger = LoggerFactory.getLogger(SocketService.class);

    @Autowired
    private SocketIOServer server;

    public void sendOperationFinishedToClient(UUID clientID, OperationFinishedResponseDto responseDto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String response = mapper.writeValueAsString(responseDto);
            server.getClient(clientID).sendEvent(SocketEventTypes.OPERATION_FINISHED.getValue(), response);

            logger.info("Successfully sent response to client {}", clientID);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendLogToClient(UUID clientID, String message) {
        server.getClient(clientID).sendEvent(SocketEventTypes.BACKEND_LOG.getValue(), message);
    }
}
