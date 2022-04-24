package at.fhburgenland.rabbitmq;

import at.fhburgenland.model.Model;
import at.fhburgenland.model.ModelMQ;
import at.fhburgenland.model.RestMethod;
import com.google.gson.Gson;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    /*
    @Autowired
    private at.fhburgenland.service.Service service;
     */

    public void onMessage(Message message) {
        System.out.println("Consuming Message - " + new String(message.getBody()));
        /*
        Gson gson = new Gson();
        String jsonInString = new String(message.getBody());
        ModelMQ messageMQ = gson.fromJson(jsonInString, ModelMQ.class);
        System.out.println("Converted GSON: " + messageMQ);
        if (messageMQ.getMethod().equals(RestMethod.POST)) {
            service.addData(new Model(messageMQ.getId(), messageMQ.getUsername(), messageMQ.getStatustext(), messageMQ.getUhrzeit()));
        } else if (messageMQ.getMethod().equals(RestMethod.PUT)) {
            try {
                service.updateData(messageMQ.getQueryId(), new Model(messageMQ.getId(), messageMQ.getUsername(), messageMQ.getStatustext(), messageMQ.getUhrzeit()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (messageMQ.getMethod().equals(RestMethod.DELETE)) {
            try {
                service.deleteData(messageMQ.getQueryId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
         */
    }

}