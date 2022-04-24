package at.fhburgenland.controller;

import at.fhburgenland.model.Model;
import at.fhburgenland.model.ModelMQ;
import at.fhburgenland.model.RestMethod;
import at.fhburgenland.rabbitmq.RabbitMQSender;
import at.fhburgenland.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private Service service;

    public Controller(Service service) {
        this.service = service;
    }

    /**
     * HTTP GET
     * retrieves all data
     *
     * @return String
     */
    @GetMapping
    public List<Model> getData() {
        return service.getData();
    }

    /**
     * HTTP POST
     * adds new entry
     *
     * @param model object, which is to be added to the list
     * @return String
     */
    @PostMapping
    public List<Model> addData(@RequestBody Model model) {
        model.setUhrzeit(LocalDateTime.now(ZoneId.of("Europe/Vienna")));
        rabbitMQSender.sendMessage(new ModelMQ(model.getId(), model.getUsername(), model.getStatustext(), model.getUhrzeit(), null, RestMethod.POST));
        return service.addData(model);
    }

    /**
     * HTTP PUT
     * updates existing entry
     *
     * @param id       id of list entry, which is to be updated
     * @param newModel updated object
     * @return String
     */
    @PutMapping(path = "{id}")
    public List<Model> updateData(@PathVariable("id") Integer id, @RequestBody Model newModel) throws Exception {
        newModel.setUhrzeit(LocalDateTime.now(ZoneId.of("Europe/Vienna")));
        rabbitMQSender.sendMessage(new ModelMQ(newModel.getId(), newModel.getUsername(), newModel.getStatustext(), newModel.getUhrzeit(), id, RestMethod.PUT));
        return service.updateData(id, newModel);
    }

    /**
     * HTTP DELETE
     * deletes existing entry
     *
     * @param id id of list entry, which is to be deleted
     * @return String
     */
    @DeleteMapping(path = "{id}")
    public List<Model> deleteData(@PathVariable("id") Integer id) throws Exception {
        rabbitMQSender.sendMessage(new ModelMQ(null, null, null, null, id, RestMethod.DELETE));
        return service.deleteData(id);
    }

}
