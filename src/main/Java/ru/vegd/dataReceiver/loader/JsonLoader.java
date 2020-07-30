package ru.vegd.dataReceiver.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.springframework.http.HttpStatus;
import ru.vegd.dataReceiver.receivingDataExceptions.ResponseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Receive json data from URL.
 */
public class JsonLoader implements Callable<JsonArray> {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JsonLoader.class.getName());

    private static final Integer SECONDS_TO_SLEEP_505_ERROR_CODE = 30;
    private static final Integer SLEEP_TIME_IN_SECONDS = 1;
    private static final Integer MAX_ATTEMPS_NUM = 2;

    private String name;
    private String link;

    /**
     * @param name - Name of thread.
     * @param link - Link to download.
     */
    public JsonLoader(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    /**
     * Download data received through API request. And parse it to {@link JsonArray}.
     * @return JsonArray which contains json objects received from request.
     * @throws ResponseException if request returned 505 error more then 2 times.
     */
    @Override
    public JsonArray call() throws Exception {
        DefaultHttpClient httpClient = null;
        HttpResponse response = null;
        JsonArray jsonArray = null;
        try {
            httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
            HttpGet getRequest = new HttpGet(
                    link);
            getRequest.addHeader("accept", "application/json");
            // TODO:
            for (Integer i = 0; i < MAX_ATTEMPS_NUM; i++) {
                response = httpClient.execute(getRequest);
                HttpStatus httpCode = HttpStatus.resolve(response.getStatusLine().getStatusCode());

                if (!httpCode.is2xxSuccessful()) {
                    if (httpCode.equals(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS.value())) {
                        TimeUnit.SECONDS.sleep(SLEEP_TIME_IN_SECONDS);
                    }
                    if (httpCode.is5xxServerError()) {
                        logger.warn(httpCode + "HTTP error. Trying to reconnect");
                        TimeUnit.SECONDS.sleep(SECONDS_TO_SLEEP_505_ERROR_CODE * i);
                        if (i > 3) {
                            throw new ResponseException("Failed 5xx : HTTP error code", httpCode.value());
                        }
                    if (httpCode.is4xxClientError()) {
                        throw new ResponseException("Failed 4xx : HTTP error code", httpCode.value());
                    }
                    }
                } else {
                    if (httpCode.is2xxSuccessful()) {
                        i = MAX_ATTEMPS_NUM;
                    } else {
                        throw new ResponseException("Failed : HTTP error code", httpCode.value());
                    }
                }
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((Objects.requireNonNull(response).getEntity().getContent())));
            jsonArray = JsonParser.parseReader(br).getAsJsonArray();
            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            logger.warn("Connection error");
        }
        return jsonArray;
    }
}
