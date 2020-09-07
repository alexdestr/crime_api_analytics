package ru.vegd.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vegd.entity.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class MainController {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(MainController.class.getName());

    @PostMapping("/api")
    public String getApiResponse(HttpServletRequest request) {
        RequestBody requestBody = new RequestBody();
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

        requestBody.setReportName(json.get("report")
                .getAsJsonObject().get("name").toString().replaceAll("\"", ""));
        requestBody.setReportDescription(json.get("report")
                .getAsJsonObject().get("description").toString().replaceAll("\"", ""));

        for (Integer i = 0; i < json.get("inputs").getAsJsonArray().size(); i++) {
            requestBody.addInputs();

            requestBody.setInputsVar(i, json.get("inputs").getAsJsonArray().get(i)
                    .getAsJsonObject().get("var").toString().replaceAll("\"", ""));
            requestBody.setInputsLabel(i, json.get("inputs").getAsJsonArray().get(i)
                    .getAsJsonObject().get("label").toString().replaceAll("\"", ""));
            requestBody.setInputsType(i, json.get("inputs").getAsJsonArray().get(i)
                    .getAsJsonObject().get("type").toString().replaceAll("\"", ""));
        }

        for (Integer i = 0; i < json.get("outputs").getAsJsonArray().size(); i++) {
            requestBody.addOutputs();

            requestBody.setOutputsIndex(i, json.get("outputs").getAsJsonArray().get(i)
                    .getAsJsonObject().get("index").getAsInt());
            requestBody.setOutputsLabel(i, json.get("outputs").getAsJsonArray().get(i)
                    .getAsJsonObject().get("label").toString());
            requestBody.setOutputsType(i, json.get("outputs").getAsJsonArray().get(i)
                    .getAsJsonObject().get("type").toString());
        }

        return "answer";
    }
}
