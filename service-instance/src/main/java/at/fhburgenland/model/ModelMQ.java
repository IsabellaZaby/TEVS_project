package at.fhburgenland.model;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public class ModelMQ extends Model {

    @Value("${server.port}")
    private String port;

    private final Integer queryId;
    private final RestMethod method;

    public ModelMQ(Integer id, String username, String statustext, LocalDateTime uhrzeit, Integer queryId, RestMethod method) {
        super(id, username, statustext, uhrzeit);
        this.queryId = queryId;
        this.method = method;
    }

    public String getPort() {
        return port;
    }

    public Integer getQueryId() {
        return queryId;
    }

    public RestMethod getMethod() {
        return method;
    }

}
