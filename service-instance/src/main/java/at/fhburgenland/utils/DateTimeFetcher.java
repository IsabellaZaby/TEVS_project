package at.fhburgenland.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * @author Stefan Gass, Isabella Zaby
 * @version 1.0
 * <p>
 * This class provides an api to fetch date and time from <a href="http://worldtimeapi.org">http://worldtimeapi.org</a>
 * @since April 2022
 */

public class DateTimeFetcher {

    public static LocalDateTime fetchDateTime() throws IOException {
        Gson gson = new Gson();
        JsonObject js = gson.fromJson(fetchWebsite("http://worldtimeapi.org/api/timezone/Europe/Vienna"), JsonObject.class);
        String dateTime = js.get("datetime").toString().split("\\.")[0].substring(1);
        return LocalDateTime.parse(dateTime);
    }

    private static String fetchWebsite(String link) throws IOException {
        URLConnection connection;
        connection = new URL(link).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        String content = scanner.next();
        scanner.close();
        return content;
    }

}
