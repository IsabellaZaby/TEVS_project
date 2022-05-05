package at.fhburgenland.controller;

import at.fhburgenland.model.Model;
import at.fhburgenland.model.ModelMQ;
import at.fhburgenland.model.RestMethod;
import at.fhburgenland.rabbitmq.RabbitMQSender;
import at.fhburgenland.service.RestService;
import at.fhburgenland.utils.DateTimeFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class manages the HTTP methods (GET, POST, PUT, UPDATE).
 * @since April 2022
 */

@RestController
public class Controller {

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private RestService restService;

    @Value("${server.port}")
    private String port;

    /**
     * HTTP GET
     * retrieves all data
     *
     * @return List of objects
     */
    @GetMapping
    public List<Model> getData() {
        return restService.getData();
    }

    /**
     * HTTP POST
     * adds new entry
     *
     * @param model object, which is to be added to the list
     * @return List of objects
     */
    @PostMapping
    public List<Model> addData(@RequestBody Model model) throws IOException {
        model.setUhrzeit(DateTimeFetcher.fetchDateTime());
        ModelMQ message = new ModelMQ(model.getId(), model.getUsername(), model.getStatustext(), model.getUhrzeit(), RestMethod.POST, port);
        rabbitMQSender.sendMessage(message);
        return restService.addData(model);
    }

    /**
     * HTTP PUT
     * updates existing entry
     *
     * @param id       id of list entry, which is to be updated
     * @param newModel updated object
     * @return List of objects
     */
    @PutMapping(path = "{id}")
    public List<Model> updateData(@PathVariable("id") Integer id, @RequestBody Model newModel) throws Exception {
        newModel.setUhrzeit(DateTimeFetcher.fetchDateTime());
        ModelMQ message = new ModelMQ(newModel.getId(), newModel.getUsername(), newModel.getStatustext(), newModel.getUhrzeit(), RestMethod.PUT, port);
        rabbitMQSender.sendMessage(message);
        return restService.updateData(id, newModel);
    }

    /**
     * HTTP DELETE
     * deletes existing entry
     *
     * @param id id of list entry, which is to be deleted
     * @return List of objects
     */
    @DeleteMapping(path = "{id}")
    public List<Model> deleteData(@PathVariable("id") Integer id) throws Exception {
        ModelMQ message = new ModelMQ(id, null, null, null, RestMethod.DELETE, port);
        rabbitMQSender.sendMessage(message);
        return restService.deleteData(id);
    }

}
