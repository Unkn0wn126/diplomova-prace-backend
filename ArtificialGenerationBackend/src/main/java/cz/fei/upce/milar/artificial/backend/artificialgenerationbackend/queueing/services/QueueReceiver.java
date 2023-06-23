package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_TYPE;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.TaskRequestMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class QueueReceiver implements ApplicationContextAware {

    @Autowired
    private SocketIOServer server;

    private ApplicationContext applicationContext;

    @RabbitListener(queues = "python")
    public void receiveMessage(Message message) {
        try {
            OPERATION_TYPE operationType = OPERATION_TYPE.valueOf(message.getMessageProperties().getHeader("operationType"));
            UUID operationID = message.getMessageProperties().getHeader("operationID");
            System.out.println(operationType);
            System.out.println(operationID);
            byte[] body = message.getBody();
            String result = new String(body, StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + result + "'");

            ObjectMapper mapper = new ObjectMapper();
            TaskRequestMessage taskRequestMessage = mapper.readValue(result, TaskRequestMessage.class);
            // TODO sending by client ID
            for (SocketIOClient client:
                    server.getAllClients()) {
                client.sendEvent("process_finished", taskRequestMessage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
