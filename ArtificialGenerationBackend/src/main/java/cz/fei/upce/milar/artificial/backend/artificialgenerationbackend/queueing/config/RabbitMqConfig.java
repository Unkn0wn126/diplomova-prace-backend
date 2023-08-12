package cz.fei.upce.milar.artificial.backend.artificialgenerationbackend.queueing.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    // see (same up to the Binding): https://spring.io/guides/gs/messaging-rabbitmq/
    @Value("${queues.exchange}")
    private String topicExchangeName;
    @Value("${queues.model-parsing.request}")
    private String modelParsingRequestQueueName;
    @Value("${queues.model-parsing.response}")
    private String modelParsingResponseQueueName;

    @Value("${queues.scene-generation.request}")
    private String sceneGenerationRequestQueueName;
    @Value("${queues.scene-generation.response}")
    private String sceneGenerationResponseQueueName;

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Bean
    Queue modelParsingRequestQueue() {
        System.out.println(rabbitHost);
        return new Queue(modelParsingRequestQueueName, false);
    }

    @Bean
    Queue modelParsingResponseQueue() {
        return new Queue(modelParsingResponseQueueName, false);
    }

    @Bean
    Queue sceneGenerationRequestQueue() {
        return new Queue(sceneGenerationRequestQueueName, false);
    }

    @Bean
    Queue sceneGenerationResponseQueue() {
        return new Queue(sceneGenerationResponseQueueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(@Qualifier("modelParsingRequestQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @Bean
    Binding binding1(@Qualifier("sceneGenerationRequestQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
