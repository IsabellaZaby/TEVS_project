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

    private String username;
    private String statustext;
    private LocalDateTime uhrzeit;

    public Model(String username, String statustext, LocalDateTime uhrzeit) {
        this.username = username;
        this.statustext = statustext;
        this.uhrzeit = uhrzeit;
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
        return "username = '" + username + "', statustext = '" + statustext + "', " + "uhrzeit = '" + uhrzeit + "'";
    }

}
