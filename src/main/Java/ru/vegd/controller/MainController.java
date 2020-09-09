package ru.vegd.controller;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/api")
    public String getApiResponse(HttpServletRequest request) {
        JsonObject json = RequestParser.parseRequestBodyToJsonObject(request);
        RequestBody requestBody = new JsonToEntityConverter().convertToRequestBody(json);

        Map map = new HashMap();
        map.put("id", "1");
        RequestSQLBuilder requestSQLBuilder = new RequestSQLBuilder();
        String sql = requestSQLBuilder
                .setSQL("SELECT <id> FROM users")
                .setValues(map)
                .build();


        return "answer";
    }
}
