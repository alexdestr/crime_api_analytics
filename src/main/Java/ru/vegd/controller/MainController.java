package ru.vegd.controller;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vegd.dao.impl.ApiImpl;
import ru.vegd.receiver.utils.JsonToEntityConverter;
import ru.vegd.http.RequestBody;
import ru.vegd.http.ResponseBody;
import ru.vegd.builder.RequestSQLBuilder;
import ru.vegd.utils.RequestParser;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(MainController.class.getName());

    @Autowired
    ApiImpl api;

    @PostMapping("/api")
    public String getApiResponse(HttpServletRequest request, Model model) {
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

        List<ResponseBody> responseList = api.getDataByRequest(requestBody);

        /*for (Integer z = 0; z < responseList.size(); z++) {
            for (Integer i = 0; i < responseList.get(z).size(); i++) {
                System.out.println(responseList.get(z).get(i));
            }
        }*/

        model.addAttribute("request", requestBody);
        model.addAttribute("response", responseList);

        return "answer";
    }
}
