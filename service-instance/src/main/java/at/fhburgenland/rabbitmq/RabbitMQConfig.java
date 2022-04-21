package at.fhburgenland.rabbitmq;

import com.netflix.discovery.EurekaClient;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Autowired
    EurekaClient eurekaClient;

    @Value("${test.rabbitmq.queue}")
    String queueName;

    @Value("${test.rabbitmq.sending}")
    String sending;

    @Value("${test.rabbitmq.sendingSecond}")
    String sendingTwo;

    @Value("${test.rabbitmq.exchange}")
    String exchange;

    @Bean
    Queue sendingQueue() {
        return new Queue(sending, false);
    }

    @Bean
    Queue sendingQueueTwo() {
        return new Queue(sendingTwo, false);
    }


    @Bean
    Queue listeningQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(exchange);
    }

    @Bean
    Binding binding(Queue sendingQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(sendingQueue).to(exchange);
    }

    @Bean
    Binding queueTwoBinding(Queue sendingQueueTwo, FanoutExchange exchange) {
        return BindingBuilder.bind(sendingQueueTwo).to(exchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueues(listeningQueue());
        simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
        return simpleMessageListenerContainer;

    }


    @Bean
    public AmqpTemplate rabbitTemplateTest(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}