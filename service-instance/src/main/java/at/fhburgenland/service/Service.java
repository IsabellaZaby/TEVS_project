package at.fhburgenland.service;

import at.fhburgenland.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class contains the list of objects, which is only active during runtime, as well as all the methods
 * to get, add, update und delete data from it.
 * @since April 2022
 */

@org.springframework.stereotype.Service
public class Service {

    private final List<Model> modelList = new ArrayList<>();

    public String getData() {
        StringBuilder sb = new StringBuilder();
        if (modelList.isEmpty()) {
            sb.append("No data added yet.\n");
        } else {
            for (int i = 0; i < modelList.size(); i++) {
                sb.append(i)
                        .append(": ")
                        .append(modelList.get(i).toString())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    public void addData(Model model) {
        modelList.add(model);
    }

    public void updateData (Integer id, Model newModel) {
        modelList.set(id, newModel);
    }

    public void deleteData (Integer id) {
        modelList.remove(modelList.get(id));
    }

}
