package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import ru.vegd.entity.StreetLocation;
import ru.vegd.linkBuilder.LinkBuider;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Receiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Receiver.class.getName());

    private String link;
    private List<StreetLocation> csvData;
    List<JsonArray> resultList = new ArrayList<>();

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public Receiver(String link, List<StreetLocation> csvData) {
        this.link = link;
        this.csvData = csvData;
    }

    public List<JsonArray> receiveData(YearMonth fromDate, YearMonth toDate) {
        for (YearMonth date = fromDate; !date.equals(toDate.plusMonths(1L)); date = date.plusMonths(1L)) {
            for (StreetLocation streetLocation : csvData) {
                String finalLink = new LinkBuider().setStartLink(link)
                        .setLongitude(streetLocation.getLongitude())
                        .setLatitude(streetLocation.getLatitude())
                        .setDate(date)
                        .build();
                JsonLoader jsonLoader = new JsonLoader("JsonLoader" + date.toString() + " Street: " + streetLocation.getStreetName(), finalLink);
                Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
                try {
                    resultList.add(jsonArrayFuture.get());
                } catch (InterruptedException e) {
                    logger.warn("Thread interrupted!");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    logger.warn("Execution interrupted!");
                    e.printStackTrace();
                }
            }
        }
        executor.shutdown();
        return resultList;
    }

    public List<JsonArray> receiveData() {
            JsonLoader jsonLoader = new JsonLoader("JsonLoader", link);
            Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
            try {
                resultList.add(jsonArrayFuture.get());
            } catch (InterruptedException e) {
                logger.warn("Thread interrupted!");
                e.printStackTrace();
            } catch (ExecutionException e) {
                logger.warn("Execution interrupted!");
                e.printStackTrace();
            }
        executor.shutdown();
        return resultList;
    }
}

