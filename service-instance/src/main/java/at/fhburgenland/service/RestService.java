package at.fhburgenland.service;

import at.fhburgenland.model.Model;
import at.fhburgenland.model.ModelMQ;
import at.fhburgenland.model.RestMethod;
import com.google.gson.JsonObject;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class contains the list of objects, which is only active during runtime, as well as all the methods
 * to get, add, update und delete data from it.
 * @since April 2022
 */

@Service("restService")
public class RestService {

    @Value("${server.port}")
    private String port;

    private List<Model> modelList = new ArrayList<>();

    /**
     * Provides method to reload data from "personal" database if the service crashed during execution.
     * REST requests that were executed during the server was down will stay in the queue until the server was restarted.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadJSONAfterStartup() {
        File newFile = new File("src/main/resources/data" + port + ".json");
        if (newFile.length() > 0) {
            JSONService jsonService = JSONService.getInstance();
            try {
                this.modelList = jsonService.readFromJson(port);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * HTTP GET service
     * retrieves all data
     *
     * @return List of objects
     */
    public List<Model> getData() {
        if (modelList.isEmpty()) {
            List<Model> emptyList = new ArrayList<>();
            emptyList.add(new Model(null, "No data added yet", null, null));
            return emptyList;
        } else {
            return modelList;
        }
    }

    /**
     * HTTP POST service
     * adds new entry
     *
     * @param model object, which is to be added to the list
     * @return List of objects
     */
    public List<Model> addData(Model model) throws Exception {
        model.setId(generateId());
        modelList.add(model);
        JSONService jsonService = JSONService.getInstance();
        jsonService.writeToJson(modelList, port);
        return modelList;
    }

    /**
     * HTTP PUT service
     * updates existing entry
     *
     * @param id       id of list entry, which is to be updated
     * @param newModel updated object
     * @return List of objects
     */
    public List<Model> updateData(Integer id, Model newModel) throws Exception {
        Integer index = getIndex(id);
        if (index != null) {
            modelList.set(index, newModel);
        } else {
            throw new Exception("Could not match index");
        }
        JSONService jsonService = JSONService.getInstance();
        jsonService.writeToJson(modelList, port);
        return modelList;
    }

    /**
     * HTTP DELETE
     * deletes existing entry
     *
     * @param id id of list entry, which is to be deleted
     * @return List of objects
     */
    public List<Model> deleteData(Integer id) throws Exception {
        Integer index = getIndex(id);
        if (index != null) {
            modelList.remove(modelList.get(index));
        } else {
            throw new Exception("Could not match index");
        }
        JSONService jsonService = JSONService.getInstance();
        jsonService.writeToJson(modelList, port);
        return modelList;
    }

    /**
     * Generate an integer depending on the last entry of the list.
     *
     * @return ID as integer
     */
    private Integer generateId() {
        if (modelList.isEmpty()) {
            return 0;
        } else {
            return getNextId();
        }
    }

    /**
     * Get the index of an entry within the array list.
     *
     * @param id the number we are looking for
     * @return the index of the entry we are looking for
     */
    private Integer getIndex(Integer id) {
        for (int i = 0; i < modelList.size(); i++) {
            if (Objects.equals(modelList.get(i).getId(), id)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Add/change/delete entries from the list, depending on the RabbitMQ message.
     * Ignore the message, if it is coming from yourself.
     *
     * @param message containing ModelMQ object
     */
    public void getUpdateData(Message message) {
        System.out.println(new String(message.getBody()));
        JSONService jsonService = JSONService.getInstance();
        JsonObject js = jsonService.parseBody(message.getBody());
        Integer id = !(js.get("id").isJsonNull()) ? js.get("id").getAsInt() : generateId();

        if (!this.port.equals(js.get("port").getAsString())) {
            switch (RestMethod.valueOf(js.get("method").getAsString().toUpperCase())) {
                case PUT: {
                    ModelMQ modelMQ = createModel(id, js);
                    Model modelToPut = this.modelList.stream().filter(model -> Objects.equals(model.getId(), id)).findFirst().orElse(null);
                    int idx = this.modelList.indexOf(modelToPut);
                    this.modelList.set(idx, new Model(modelMQ.getId(), modelMQ.getUsername(), modelMQ.getStatustext(), modelMQ.getUhrzeit()));
                    try {
                        jsonService.writeToJson(modelList, port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case POST: {
                    ModelMQ modelMQ = createModel(id, js);
                    this.modelList.add(new Model(this.modelList.size(), modelMQ.getUsername(), modelMQ.getStatustext(), modelMQ.getUhrzeit()));
                    try {
                        jsonService.writeToJson(modelList, port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case DELETE: {
                    // TODO bug - delete does not work properly...
                    Model modelToDelete = this.modelList.stream().filter(model -> Objects.equals(model.getId(), id)).findFirst().orElse(null);
                    this.modelList.remove(modelToDelete);
                    try {
                        jsonService.writeToJson(modelList, port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private String parseTime(String input) {
        Integer time = Integer.parseInt(input);
        if (time <= 9) {
            return "0" + time;
        } else {
            return String.valueOf(time);
        }
    }

    /**
     * Create the matching ModelMQ from content of the RabbitMQ message.
     *
     * @param id the id of the Model, which will be created
     * @param js the json content of the RabbitMQ message
     * @return the corresponding ModelMQ object
     */
    private ModelMQ createModel(Integer id, JsonObject js) {
        JsonObject time = (JsonObject) js.get("uhrzeit");
        String str = time.get("year") + "-" + time.get("monthValue") + "-" +
                time.get("dayOfMonth") + " " + parseTime(time.get("hour").toString()) + ":" + parseTime(time.get("minute").toString()) + ":" + parseTime(time.get("second").toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        ModelMQ modelMQ = new ModelMQ(id, js.get("username").getAsString(),
                js.get("statustext").getAsString(), dateTime,
                RestMethod.valueOf(js.get("method").getAsString().toUpperCase()), js.get("port").getAsString());
        System.out.println(modelMQ);
        return modelMQ;
    }

    private int getNextId() {
        return this.modelList.get(this.modelList.size() - 1).getId() + 1;
    }

}
