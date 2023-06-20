package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class QueueReceiver {

    @Autowired
    private SocketIOServer server;

    @RabbitListener(queues = "python")
    public void receiveMessage(Message message) {
        try {
            byte[] body = message.getBody();
            String result = new String(body, StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + result + "'");
            // TODO sending by client ID
            for (SocketIOClient client:
                    server.getAllClients()) {
                client.sendEvent("process_finished", result);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
