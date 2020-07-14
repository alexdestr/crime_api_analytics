package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.CrimeCategoriesDao;
import ru.vegd.dataReceiver.loader.JsonLoader;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.CrimeCategory;
import ru.vegd.entity.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Processes data and inserts into the database.
 */
public class CrimeCategoriesReceiver {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(CrimeCategoriesReceiver.class.getName());

    private static final Integer threadNum = Runtime.getRuntime().availableProcessors() + 1; // optimal number of threads
    private static final String link = "https://data.police.uk/api/crime-categories";

    private CrimeCategoriesDao crimeCategoriesDao;

    private List<Station> csvData;

    private ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<JsonArray> jsonArray = new ArrayList<>();

    /**
     * @param csvData list of police stations and their coordinates.
     * @param crimeCategoriesDao DAO with injected connection to load data into a database.
     */
    public CrimeCategoriesReceiver(List<Station> csvData, CrimeCategoriesDao crimeCategoriesDao) {
        this.csvData = csvData;
        this.crimeCategoriesDao = crimeCategoriesDao;
    }

    /**
     * Pulls data from thread, convert to an entity and inserts into to database.
     */
    public void receiveData() {
        JsonLoader jsonLoader = new JsonLoader("JsonLoader", link);
        Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);

        try {
            jsonArray.add(jsonArrayFuture.get());
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
        List<CrimeCategory> resultList = new ArrayList<>();
        for (JsonArray json : jsonArray) {
            for (Integer i = 0; i < json.size(); i++) {
                resultList.add(new JsonToEntityConverter().convertToCrimeCategories(json.get(i).getAsJsonObject()));
            }
        }
        crimeCategoriesDao.add(resultList);
    }
}
