package ru.vegd.receiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.receiver.loader.JsonLoader;
import ru.vegd.receiver.utils.JsonToEntityConverter;
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

    private ForcesListDAO forcesListDAO;

    private List<Station> csvData;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<Force> resultList = new ArrayList<>();

    /**
     * @param csvData list of police stations and their coordinates.
     * @param forcesListDAO DAO with injected connection to load data into a database.
     */
    public ForcesListReceiver(List<Station> csvData, ForcesListDAO forcesListDAO) {
        this.csvData = csvData;
        this.forcesListDAO = forcesListDAO;
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
            forcesListDAO.add(resultList);
        } catch (InterruptedException e) {
            logger.warn("Thread interrupted in Json Loader: " + jsonLoader.getName());
            e.printStackTrace();
        } catch (ExecutionException e) {
            logger.warn("Execution interrupted in Json Loader: " + jsonLoader.getName());
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
