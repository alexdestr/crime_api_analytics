package ru.vegd.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestParser {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(RequestParser.class.getName());

    public static JsonObject parseRequestBodyToJsonObject(HttpServletRequest request) {
        JsonArray jsonArray = new JsonArray();
        StringBuilder jsonString = new StringBuilder();

        try {
            Integer ch = -1;
            while ((ch = request.getInputStream().read()) != -1) {
                jsonString.append(Character.toChars(ch));
            }
        } catch (IOException e) {
            logger.warn("IO Exception: " + e.getMessage());
        }

        jsonArray.add(JsonParser.parseString(jsonString.toString()));
        JsonObject json = jsonArray.get(0).getAsJsonObject();

        return json;
    }
}
