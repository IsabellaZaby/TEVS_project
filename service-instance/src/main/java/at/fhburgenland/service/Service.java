package at.fhburgenland.service;

import at.fhburgenland.model.Model;

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

@org.springframework.stereotype.Service
public class Service {

    private final List<Model> modelList = new ArrayList<>();

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

    public List<Model> updateData (Integer id, Model newModel) throws Exception {
        if (getIndex(id) != null) {
            modelList.set(getIndex(id), newModel);
        } else {
            throw new Exception("Could not match index");
        }
        return modelList;
    }

    public List<Model> deleteData (Integer id) throws Exception {
        if (getIndex(id) != null) {
            modelList.remove(modelList.get(getIndex(id)));
        } else {
            throw new Exception("Could not match index");
        }
        return modelList;
    }

    private Integer generateId() {
        if (modelList.isEmpty()) {
            return 0;
        } else {
            return modelList.get(modelList.size()-1).getId() + 1;
        }
    }

    private Integer getIndex (Integer id) {
        for (int i = 0; i < modelList.size(); i++) {
            if (Objects.equals(modelList.get(i).getId(), id)) {
                return i;
            }
        }
        return null;
    }

}
