package at.fhburgenland.controller;

import at.fhburgenland.model.Model;
import at.fhburgenland.rabbitmq.RabbitMQSender;
import at.fhburgenland.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String getData() {
        // public String getData(HttpServletRequest request) {
        // rabbitMQSender.send(request.getRequestURL().toString());
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
    public String addData(@RequestBody Model model) {
        service.addData(model);
        // TODO add rabbitmq message
        // rabbitMQSender.sendMessage(model);
        return "Added entry " + model.toString();
    }

    /**
     * HTTP PUT
     * updates existing entry
     *
     * @param id id of list entry, which is to be updated
     * @param newModel updated object
     * @return String
     */
    @PutMapping(path = "{id}")
    public String updateData(@PathVariable("id") Integer id, @RequestBody Model newModel) {
        service.updateData(id, newModel);
        // TODO add rabbitmq message
        //rabbitMQSender.sendMessage(model);
        return "Updated entry with id " + id;
    }

    /**
     * HTTP DELETE
     * deletes existing entry
     *
     * @param id id of list entry, which is to be deleted
     * @return String
     */
    @DeleteMapping(path = "{id}")
    public String deleteData(@PathVariable("id") Integer id) {
        service.deleteData(id);
        // TODO add rabbitmq message
        //rabbitMQSender.sendMessage(model);
        return "Deleted entry with id: " + id;
    }

}
