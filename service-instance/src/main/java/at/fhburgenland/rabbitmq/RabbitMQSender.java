package at.fhburgenland.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplateTest;

    @Value("${test.rabbitmq.exchange}")
    private String exchange;

    public void send(String test) {
        rabbitTemplateTest.convertAndSend(exchange, "", test);
        System.out.println("Send msg = " + test);

    }
}