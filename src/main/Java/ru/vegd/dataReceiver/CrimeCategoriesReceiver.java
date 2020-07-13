package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDao;
import ru.vegd.entity.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CrimeCategoriesReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CrimeCategoriesReceiver.class.getName());

    private CrimeCategoriesDao crimeCategoriesDao;

    private String link;
    private List<Station> csvData;

    private ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    private List<JsonArray> resultList = new ArrayList<>();

    public CrimeCategoriesReceiver(String link, List<Station> csvData, CrimeCategoriesDao crimeCategoriesDao) {
        this.link = link;
        this.csvData = csvData;
        this.crimeCategoriesDao = crimeCategoriesDao;
    }

    public List<JsonArray> receiveData() {
        JsonLoader jsonLoader = new JsonLoader("JsonLoader", link);
        Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);

        try {
            resultList.add(jsonArrayFuture.get());
        } catch (InterruptedException e) {
            logger.warn("Thread interrupted!");
        } catch (ExecutionException e) {
            logger.warn("Execution interrupted!");
        }
        executor.shutdown();

        final boolean done;

        try {
            done = executor.awaitTermination(csvData.size() * 11, TimeUnit.SECONDS);
            if (!done) {
                logger.warn("Not all data has been received");
            }
        } catch (InterruptedException e) {
            logger.warn("Some threads are closed ahead of schedule");
        }

        return resultList;
    }
}
