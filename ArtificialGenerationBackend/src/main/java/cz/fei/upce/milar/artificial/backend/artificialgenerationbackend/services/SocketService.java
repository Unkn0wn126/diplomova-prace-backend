package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.services;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SocketService {
    @Autowired
    private SocketIOServer server;

    public void sendMessageToClient(UUID clientID, String message) {
        server.getClient(clientID).sendEvent("message_sent", message);
    }
}
