package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.controllers.sockets;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.services.SocketService;
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
        //server.addEventListener("send_message", Message.class, onChatReceived());
    }

//    private DataListener<Message> onChatReceived() {
//        return (senderClient, data, ackSender) -> {
//            log.info(data.toString());
//            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message", data.getMessage());
//
//        };
//    }

    private ConnectListener onConnected() {
        return (client) -> {
            //log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
            client.sendEvent("after_connect", client.getSessionId().toString());
            //socketService.sendMessage("a", "after_connect", client, client.getSessionId().toString());
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            //log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }
}
