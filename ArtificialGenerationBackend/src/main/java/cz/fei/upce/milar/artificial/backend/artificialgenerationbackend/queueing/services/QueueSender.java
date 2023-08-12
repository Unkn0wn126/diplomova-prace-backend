package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GenerateScenesCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.util.SingleSceneCommandDtoFactory;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.GenerateScenesTaskRequestMessage;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_TYPE;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.ParseModelTaskRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QueueSender {
    private static Logger logger = LoggerFactory.getLogger(QueueSender.class);
    // based on this (but not much really): https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html

    @Autowired
    private RabbitTemplate template;

    @Autowired
    MessageConverter messageConverter;

    @Autowired
    @Qualifier("modelParsingRequestQueue")
    private Queue modelParsingRequestQueue;

    @Autowired
    @Qualifier("sceneGenerationRequestQueue")
    private Queue sceneGenerationRequestQueue;

    public void sendGenerateScenesCommand(UUID clientId, UUID operationID,
                                          GenerateScenesCommandDto generateScenesCommandDto) {
        for (int i = 0; i < generateScenesCommandDto.getSceneDefinition().getNumberOfScenes(); i++) {
            GenerateScenesTaskRequestMessage taskMessage = new GenerateScenesTaskRequestMessage();
            taskMessage.setClientID(clientId);
            taskMessage.setOperationID(operationID);
            taskMessage.setOperationIndex(i);
            taskMessage.setOperationType(OPERATION_TYPE.GENERATE_SCENES);
            taskMessage.setContent(SingleSceneCommandDtoFactory
                    .createFromGenerateScenesCommandDto(generateScenesCommandDto, i));

            try {
                this.template.convertAndSend(sceneGenerationRequestQueue.getName(),
                        taskMessage, messagePostProcessor -> {

                    messagePostProcessor.getMessageProperties().setHeader("operationType",
                            OPERATION_TYPE.GENERATE_SCENES);

                    messagePostProcessor.getMessageProperties().setHeader("operationID", operationID);
                    return messagePostProcessor;
                });
            } catch (AmqpException e) {
                logger.error(e.getMessage());
                // TODO inform client
            }
        }
    }

    public void sendParseFileCommand(UUID clientId, UUID operationID, ParseFileCommandDto parseFileCommandDto) {
        ParseModelTaskRequestMessage taskMessage = new ParseModelTaskRequestMessage();
        taskMessage.setClientID(clientId);
        taskMessage.setOperationID(operationID);
        taskMessage.setOperationIndex(0);
        taskMessage.setOperationType(OPERATION_TYPE.PARSE_OBJECTS);
        taskMessage.setContent(parseFileCommandDto);

        try {
            this.template.setMessageConverter(messageConverter);
            this.template.convertAndSend(modelParsingRequestQueue.getName(), taskMessage);
        } catch (AmqpException e) {
            logger.error(e.getMessage());
            // TODO inform client
        }
    }
}
