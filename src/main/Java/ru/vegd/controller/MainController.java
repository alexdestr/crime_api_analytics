package ru.vegd.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vegd.dao.Impl.ApiImpl;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.RequestBody;
import ru.vegd.sqlBuilder.RequestSQLBuilder;
import ru.vegd.utils.RequestParser;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(MainController.class.getName());

    @Autowired
    ApiImpl api;

    @PostMapping("/api")
    public String getApiResponse(HttpServletRequest request) {
        JsonObject json = RequestParser.parseRequestBodyToJsonObject(request);
        RequestBody requestBody = new JsonToEntityConverter().convertToRequestBody(json);
        Map valueMap = new HashMap();

        for (Integer i = 0; i < requestBody.getInputs().size(); i++) {
            if (!requestBody.getInputsType(i).equals("string")) {
                valueMap.put(requestBody.getInputsVar(i), requestBody.getInputsValue(i));
            } else {
                valueMap.put(requestBody.getInputsVar(i), "\'" + requestBody.getInputsValue(i) + "\'");
            }
        }

        String formattedSql =
                new RequestSQLBuilder()
                .setSQL(requestBody.getSql())
                .setValues(valueMap)
                .build();
        requestBody.setSql(formattedSql);

        for (Integer i = 0; i < api.getDataByRequest(requestBody).getOutputs().size(); i++) {
            System.out.println(api.getDataByRequest(requestBody).getOutputsValue(i));
        } // TODO: rework this :)

        return "answer";
    }
}
