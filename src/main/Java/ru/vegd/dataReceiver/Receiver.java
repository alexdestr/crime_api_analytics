package ru.vegd.dataReceiver;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Receiver {

    private String link;
    List<JsonArray> resultList;

    public Receiver(String link) {
        this.link = link;
    }

    public List<JsonArray> getData(YearMonth fromDate, YearMonth toDate) {
        for (YearMonth date = fromDate; !date.equals(toDate.plusMonths(1L)); date = date.plusMonths(1L)) {
            link = link.replace(fromDate.toString(), date.toString());
            resultList = new ArrayList();
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            JsonLoader jsonLoader = new JsonLoader("JsonLoader" + date.toString(), link);
            Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
            executor.shutdown();
            try {
                resultList.add(jsonArrayFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}

