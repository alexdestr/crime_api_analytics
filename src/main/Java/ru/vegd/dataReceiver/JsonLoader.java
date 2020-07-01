package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import ru.vegd.dataReceiver.receivingDataExceptions.ResponseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


public class JsonLoader implements Callable<JsonArray> {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JsonLoader.class.getName());

    private String name;
    private String link;

    public JsonLoader(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    @Override
    public JsonArray call() throws Exception {
        DefaultHttpClient httpClient = null;
        HttpResponse response = null;
        JsonArray jsonArray = null;
        Integer secondsToSleepOn505ErrorCode = 30;
        Integer sleepTimeInSeconds = 1;
        try {
            httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
            HttpGet getRequest = new HttpGet(
                    link);
            getRequest.addHeader("accept", "application/json");

            for (Integer i = secondsToSleepOn505ErrorCode; i <= Integer.MAX_VALUE; i = i + secondsToSleepOn505ErrorCode) {
                response = httpClient.execute(getRequest);
                Integer httpCode = response.getStatusLine().getStatusCode();

                if (httpCode != org.springframework.http.HttpStatus.OK.value()) {
                    if (httpCode == org.springframework.http.HttpStatus.TOO_MANY_REQUESTS.value()) {
                        TimeUnit.SECONDS.sleep(sleepTimeInSeconds);
                    }
                    if (httpCode >= org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                        logger.warn(httpCode + "HTTP error. Trying to reconnect");
                        TimeUnit.SECONDS.sleep(i);
                        if (i > secondsToSleepOn505ErrorCode * 2) {
                            throw new ResponseException("Failed : HTTP error code", httpCode);
                        }
                    }
                } else {
                    i = 0;
                    break;
                }
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            jsonArray = JsonParser.parseReader(br).getAsJsonArray();
            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            logger.warn("Connection error");
        }
        return jsonArray;
    }
}
