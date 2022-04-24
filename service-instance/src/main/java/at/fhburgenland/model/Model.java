package at.fhburgenland.model;

import java.time.LocalDateTime;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * Data object.
 * @since April 2022
 */

public class Model {

    private Integer id;
    private String username;
    private String statustext;
    private LocalDateTime uhrzeit;

    public Model(Integer id, String username, String statustext, LocalDateTime uhrzeit) {
        this.id = id;
        this.username = username;
        this.statustext = statustext;
        this.uhrzeit = uhrzeit;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getStatustext() {
        return statustext;
    }

    public LocalDateTime getUhrzeit() {
        return uhrzeit;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatustext(String statustext) {
        this.statustext = statustext;
    }

    public void setUhrzeit(LocalDateTime uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    @Override
    public String toString() {
        return "id: " + id + ", username = '" + username + "', statustext = '" + statustext + "', " + "uhrzeit = '" + uhrzeit + "'";
    }

}
