package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.ForcesListDao;
import ru.vegd.dataReceiver.loader.JsonLoader;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.Force;
import ru.vegd.entity.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Processes data and inserts into the database.
 */
public class ForcesListReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ForcesListReceiver.class.getName());

    private static final Integer threadNum = Runtime.getRuntime().availableProcessors() + 1; // optimal number of threads
    private static final String link = "https://data.police.uk/api/forces";

    private ForcesListDao forcesListDao;


    private List<Station> csvData;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<Force> resultList = new ArrayList<>();

    /**
     * @param csvData list of police stations and their coordinates.
     * @param forcesListDao DAO with injected connection to load data into a database.
     */
    public ForcesListReceiver(List<Station> csvData, ForcesListDao forcesListDao) {
        this.csvData = csvData;
        this.forcesListDao = forcesListDao;
    }

    /**
     * Pulls data from thread, convert to an entity and inserts into to database.
     */
    public void receiveData() {
        JsonLoader jsonLoader = new JsonLoader("JsonLoader", link);
        Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
        try {
            JsonArray jsonArray = jsonArrayFuture.get();
            for (Integer i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                resultList.add(jsonToEntityConverter.convertToForcesList(object));
            }
            forcesListDao.add(resultList);
        } catch (InterruptedException e) {
            logger.warn("Thread interrupted!");
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.warn("Execution interrupted!");
            e.printStackTrace();
        }
        executor.shutdown();
        final boolean done;
        try {
            done = executor.awaitTermination(csvData.size() * 11, TimeUnit.SECONDS);
            if (!done) {
                logger.warn("Not all data has received");
            }
        } catch (InterruptedException e) {
            logger.warn("Some threads are closed ahead of schedule");
        }
    }
}
