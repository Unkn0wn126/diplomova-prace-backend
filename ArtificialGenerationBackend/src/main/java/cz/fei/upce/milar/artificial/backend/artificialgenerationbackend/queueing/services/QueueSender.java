package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.services;

import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.GeneratePositionsCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.api.dto.ParseFileCommandDto;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.OPERATION_TYPE;
import cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.domain.TaskRequestMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QueueSender {
    // based on this (but not much really): https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html

    @Autowired
    private RabbitTemplate template;

    @Autowired
    MessageConverter messageConverter;

    @Autowired
    private Queue queue;

    public void sendGeneratePositionsCommand(UUID clientId, UUID operationID, GeneratePositionsCommandDto generatePositionsCommandDto) {
        TaskRequestMessage taskMessage = new TaskRequestMessage();
        taskMessage.setClientID(clientId);
        taskMessage.setOperationID(operationID);
        taskMessage.setOperationType(OPERATION_TYPE.GENERATE_POSITIONS);
        taskMessage.setContent(generatePositionsCommandDto);

        this.template.convertAndSend(queue.getName(), taskMessage, messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setHeader("operationType", OPERATION_TYPE.GENERATE_POSITIONS);
            messagePostProcessor.getMessageProperties().setHeader("operationID", operationID);
            return messagePostProcessor;
        });
    }

    public void sendParseFileCommand(UUID clientId, UUID operationID, ParseFileCommandDto parseFileCommandDto) {
        TaskRequestMessage taskMessage = new TaskRequestMessage();
        taskMessage.setClientID(clientId);
        taskMessage.setOperationID(operationID);
        taskMessage.setOperationType(OPERATION_TYPE.PARSE_OBJECTS);
        taskMessage.setContent(parseFileCommandDto);

        this.template.setMessageConverter(messageConverter);
        this.template.convertAndSend(queue.getName(), taskMessage);
    }
}
