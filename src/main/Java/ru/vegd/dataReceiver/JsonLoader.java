package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import ru.vegd.dataReceiver.receivingDataExceptions.ResponseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Queue;
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
        try {
            httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
            HttpGet getRequest = new HttpGet(
                    link);
            getRequest.addHeader("accept", "application/json");

            for (Integer i = secondsToSleepOn505ErrorCode; i <= Integer.MAX_VALUE; i = i + 30) {
                response = httpClient.execute(getRequest);
                Integer httpCode = response.getStatusLine().getStatusCode();

                if (httpCode != 200) {
                    if (httpCode == 429) {
                        TimeUnit.SECONDS.sleep(1);
                    }
                    if (httpCode >= 500) {
                        logger.warn(httpCode + "HTTP error. Trying to reconnect");
                        TimeUnit.SECONDS.sleep(i);
                        if (i > 60) {
                            throw new ResponseException("Failed : HTTP error code", httpCode);
                        }
                    }
                } else {
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
