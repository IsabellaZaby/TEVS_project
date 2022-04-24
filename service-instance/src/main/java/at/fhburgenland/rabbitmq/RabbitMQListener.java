package at.fhburgenland.rabbitmq;

import at.fhburgenland.service.RestService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class acts as the listener for the rabbitmq message broker.
 * @since April 2022
 */
@Service
public class RabbitMQListener implements MessageListener {

    private final RestService restService;

    public RabbitMQListener(RestService restService) {
        this.restService = restService;
    }

    public void onMessage(Message message) {
        System.out.println("Consuming Message - " + new String(message.getBody()));
        restService.getUpdateData(message);


    }

}