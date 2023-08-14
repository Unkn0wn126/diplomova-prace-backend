package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.ActiveEndpointsService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.services.TempFileService;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.GenerateScenesTaskResponseMessage;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_RESULT;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_TYPE;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.ParseModelTaskResponseMessage;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.util.ReceivedMessageHelper;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.dto.OperationFinishedResponseDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.sockets.services.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class QueueReceiver implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(QueueReceiver.class);

    @Autowired
    private SocketIOServer server;

    @Autowired
    private TempFileService tempFileService;

    @Autowired
    private ActiveEndpointsService activeEndpointsService;

    @Autowired
    private SocketService socketService;

    private ApplicationContext applicationContext;

    @RabbitListener(queues = "${queues.model-parsing.response}")
    public void receiveParseModelResultMessage(Message message) {
        SocketIOClient client = null;
        UUID operationID = null;
        try {
            ReceivedMessageHelper receivedMessageHelper = extractMessageData(message);

            client = server.getClient(receivedMessageHelper.CLIENT_ID);
            operationID = receivedMessageHelper.OPERATION_ID;

            logger.info("Received parsed model for operation {} processing", receivedMessageHelper.OPERATION_ID);
            socketService.sendLogToClient(client.getSessionId(), "Received parsed model, processing...");

            String result = receivedMessageHelper.dataAsString(StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            ParseModelTaskResponseMessage taskResponseMessage = mapper.readValue(result,
                    ParseModelTaskResponseMessage.class);

            if (receivedMessageHelper.RESULT != OPERATION_RESULT.SUCCESS) {
                throw new RuntimeException(taskResponseMessage.getMessage());
            }

            tempFileService.saveModelToTempFile(receivedMessageHelper.CLIENT_ID,
                    receivedMessageHelper.OPERATION_ID, taskResponseMessage);

            socketService.sendOperationFinishedToClient(client.getSessionId(),
                    new OperationFinishedResponseDto(operationID, true, taskResponseMessage.getMessage(),
                            activeEndpointsService.composeModelResultPath() + "/" +
                                    receivedMessageHelper.OPERATION_ID + "?index=" +
                                    taskResponseMessage.getOperationIndex()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (client != null) {
                socketService.sendOperationFinishedToClient(client.getSessionId(),
                        new OperationFinishedResponseDto(operationID, false, e.getMessage(), null));
            } else {
                logger.error("Could not send response to client, as it was null");
            }
        }
    }

    @RabbitListener(queues = "${queues.scene-generation.response}")
    public void receiveGenerateSceneResultMessage(Message message) {
        SocketIOClient client = null;
        UUID operationID = null;
        try {
            ReceivedMessageHelper receivedMessageHelper = extractMessageData(message);

            client = server.getClient(receivedMessageHelper.CLIENT_ID);
            operationID = receivedMessageHelper.OPERATION_ID;

            logger.info("Received generated scene for operation {} processing", receivedMessageHelper.OPERATION_ID);
            socketService.sendLogToClient(client.getSessionId(), "Received generated scene, processing...");

            String result = receivedMessageHelper.dataAsString(StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            GenerateScenesTaskResponseMessage taskResponseMessage = mapper.readValue(result,
                    GenerateScenesTaskResponseMessage.class);

            if (receivedMessageHelper.RESULT != OPERATION_RESULT.SUCCESS) {
                throw new RuntimeException(taskResponseMessage.getMessage());
            }

            tempFileService.saveSceneToTempFile(receivedMessageHelper.CLIENT_ID,
                    receivedMessageHelper.OPERATION_ID, taskResponseMessage);

            socketService.sendOperationFinishedToClient(client.getSessionId(),
                    new OperationFinishedResponseDto(operationID, true, taskResponseMessage.getMessage(),
                            activeEndpointsService.composeSceneResultPath() + "/" +
                                    receivedMessageHelper.OPERATION_ID + "?index=" +
                                    taskResponseMessage.getOperationIndex()));
        } catch (Exception e) {
            if (client != null) {
                socketService.sendOperationFinishedToClient(client.getSessionId(),
                        new OperationFinishedResponseDto(operationID, false, e.getMessage(), null));
            } else {
                logger.error("Could not send response to client, as it was null");
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private ReceivedMessageHelper extractMessageData(Message message) {
        OPERATION_TYPE operationType =
                OPERATION_TYPE.valueOf(message.getMessageProperties().getHeader("operationType"));

        UUID operationID = UUID.fromString(message.getMessageProperties().getHeader("operationID"));

        UUID clilentID = UUID.fromString(message.getMessageProperties().getHeader("clientID"));

        OPERATION_RESULT operationResult =
                OPERATION_RESULT.valueOf(message.getMessageProperties().getHeader("operationResult"));

        byte[] body = message.getBody();

        return new ReceivedMessageHelper(operationType, operationID, clilentID, operationResult, body);
    }
}
