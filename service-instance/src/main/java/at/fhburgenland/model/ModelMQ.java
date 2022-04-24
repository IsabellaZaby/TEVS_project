package at.fhburgenland.model;

import java.time.LocalDateTime;

public class ModelMQ extends Model {

    private final String port;
    private final RestMethod method;

    public ModelMQ(Integer id, String username, String statustext, LocalDateTime uhrzeit, RestMethod method, String port) {
        super(id, username, statustext, uhrzeit);
        this.method = method;
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public RestMethod getMethod() {
        return method;
    }

}