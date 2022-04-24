package at.fhburgenland.service;

import at.fhburgenland.model.Model;
import at.fhburgenland.model.ModelMQ;
import at.fhburgenland.model.RestMethod;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public List<Model> getData() {
        if (modelList.isEmpty()) {
            List<Model> emptyList = new ArrayList<>();
            emptyList.add(new Model(null, "No data added yet", null, null));
            return emptyList;
        } else {
            return modelList;
        }
    }

    public List<Model> addData(Model model) {
        model.setId(generateId());
        modelList.add(model);
        return modelList;
    }

    public List<Model> updateData(Integer id, Model newModel) throws Exception {
        Integer index = getIndex(id);
        if (index != null) {
            modelList.set(index, newModel);
        } else {
            throw new Exception("Could not match index");
        }
        return modelList;
    }

    public List<Model> deleteData(Integer id) throws Exception {
        Integer index = getIndex(id);
        if (index != null) {
            modelList.remove(modelList.get(index));
        } else {
            throw new Exception("Could not match index");
        }
        return modelList;
    }

    private Integer generateId() {
        if (modelList.isEmpty()) {
            return 0;
        } else {
            return modelList.get(modelList.size() - 1).getId() + 1;
        }
    }

    private Integer getIndex(Integer id) {
        for (int i = 0; i < modelList.size(); i++) {
            if (Objects.equals(modelList.get(i).getId(), id)) {
                return i;
            }
        }
        return null;
    }

    public void getUpdateData(Message message) {
        System.out.println(new String(message.getBody()));
        System.out.println("#####");
        Gson gson = new Gson();
        JsonObject js = gson.fromJson(new String(message.getBody()), JsonObject.class);
        Integer id = !(js.get("id")).isJsonNull() ? js.get("id").getAsInt() : this.modelList.size();

        if (!this.port.equals(js.get("port").getAsString())) {
            switch (RestMethod.valueOf(js.get("method").getAsString().toUpperCase())) {
                case PUT: {
                    ModelMQ modelMQ = createModel(id, js);
                    this.modelList.set(modelMQ.getId(), new Model(modelMQ.getId(), modelMQ.getUsername(), modelMQ.getStatustext(), modelMQ.getUhrzeit()));
                    break;
                }
                case POST: {
                    ModelMQ modelMQ = createModel(id, js);
                    this.modelList.add(new Model(this.modelList.size(), modelMQ.getUsername(), modelMQ.getStatustext(), modelMQ.getUhrzeit()));
                    break;
                }
                case DELETE:
                    this.modelList.remove(this.modelList.get(id));
                    break;
            }
        }
    }

    private ModelMQ createModel(Integer id, JsonObject js) {
        JsonObject time = (JsonObject) js.get("uhrzeit");
        String str = time.get("year") + "-" + time.get("monthValue") + "-" +
                time.get("dayOfMonth") + " " + time.get("hour") + ":" + time.get("minute") + ":" + time.get("second");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:m:s");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        ModelMQ modelMQ = new ModelMQ(id, js.get("username").getAsString(),
                js.get("statustext").getAsString(), dateTime,
                RestMethod.valueOf(js.get("method").getAsString().toUpperCase()), js.get("port").getAsString());
        System.out.println(modelMQ);
        return modelMQ;
    }

}
