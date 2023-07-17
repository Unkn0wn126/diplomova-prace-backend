package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services;

import com.corundumstudio.socketio.SocketIOServer;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util.SocketEventTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SocketService {
    @Autowired
    private SocketIOServer server;

    public void sendLogToClient(UUID clientID, String message) {
        server.getClient(clientID).sendEvent(SocketEventTypes.BACKEND_LOG.getValue(), message);
    }
}
