package at.fhburgenland.rabbitmq;

import at.fhburgenland.model.ModelMQ;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class acts as the sender for the configuration for the rabbitmq message broker.
 * @since April 2022
 */

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplateTest;

    @Value("${test.rabbitmq.exchange}")
    private String exchange;

    public void sendMessage(ModelMQ message) {
        rabbitTemplateTest.convertAndSend(exchange, "", message);
        System.out.println("Send msg -> " + message);
    }

}