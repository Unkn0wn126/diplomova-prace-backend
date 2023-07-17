package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.models.dto.ModelDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_TYPE;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.TaskRequestMessage;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.util.SocketEventTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class QueueReceiver implements ApplicationContextAware {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private TempFileService tempFileService;

    private ApplicationContext applicationContext;

    @RabbitListener(queues = "python")
    public void receiveMessage(Message message) {
        try {
            OPERATION_TYPE operationType = OPERATION_TYPE.valueOf(message.getMessageProperties().getHeader("operationType"));
            UUID operationID = UUID.fromString(message.getMessageProperties().getHeader("operationID"));
            UUID clilentID = UUID.fromString(message.getMessageProperties().getHeader("clientID"));

            SocketIOClient client = server.getClient(clilentID);

            client.sendEvent(SocketEventTypes.BACKEND_LOG.getValue(), "Received parsed model, processing...");

            System.out.println(operationType);
            System.out.println(operationID);
            byte[] body = message.getBody();
            String result = new String(body, StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            if (operationType == OPERATION_TYPE.PARSE_OBJECTS) {
                ModelDto modelDto = mapper.readValue(result, ModelDto.class);

                System.out.println(modelDto);
                // save it to file somewhere
                tempFileService.saveModelToTempFile(operationID, modelDto);
                /*try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                    oos.writeObject(modelDto);
                    tempFileService.saveTempFile(operationID, bos.toByteArray());
                }*/
            }

            client.sendEvent(SocketEventTypes.PROCESS_FINISHED.getValue(), operationID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
