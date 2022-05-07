package at.fhburgenland.service;

import at.fhburgenland.model.Model;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service("jsonService")
public class JSONService {

    private static JSONService jsonService;

    private JSONService() {}

    public static JSONService getInstance() {
        if (jsonService == null) {
            jsonService = new JSONService();
        }
        return jsonService;
    }

    public void writeToJson(List<Model> modelList, String port) throws Exception {
        Gson gson = new Gson();
        String list = gson.toJson(modelList);
        Files.write(Paths.get("src/main/resources/data" + port + ".json"), list.getBytes());
    }

    public List<Model> readFromJson(String port) throws FileNotFoundException {
        Type listOfMyClassObject = new TypeToken<ArrayList<Model>>() {}.getType();

        FileReader reader = new FileReader("src/main/resources/data" + port + ".json");
        Gson gson = new Gson();
        return gson.fromJson(reader, listOfMyClassObject);
    }

    public JsonObject parseBody(byte[] body) {
        Gson gson = new Gson();
        return gson.fromJson(new String(body), JsonObject.class);
    }
}
