package at.fhburgenland.controller;

import at.fhburgenland.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller {
    @Autowired
    RabbitMQSender rabbitMQSender;

    @GetMapping("/readAll")
    public String testService(HttpServletRequest request) throws Exception {
        rabbitMQSender.send(request.getRequestURL().toString());
        return request.getRequestURL().toString();
    }
}
