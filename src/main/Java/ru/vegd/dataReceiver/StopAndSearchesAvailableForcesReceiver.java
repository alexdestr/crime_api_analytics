package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.dataReceiver.loader.JsonLoader;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.AvailableStopAndSearchesByForce;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Processes data and inserts into the database.
 */
public class StopAndSearchesAvailableForcesReceiver {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(StopAndSearchesAvailableForcesReceiver.class.getName());

    private static final Integer threadNum = 1; // Multithreading is not needed here, due to the fact that you only need to download 1 json object
    private static final String link = "https://data.police.uk/api/crimes-street-dates";

    private StopAndSearchesByForceDAO stopAndSearchesByForceDAO;

    private ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<JsonArray> jsonArray = new ArrayList<>();

    /**
     * @param stopAndSearchesByForceDAO DAO with injected connection to load data into a database.
     */
    public StopAndSearchesAvailableForcesReceiver(StopAndSearchesByForceDAO stopAndSearchesByForceDAO) {
        this.stopAndSearchesByForceDAO = stopAndSearchesByForceDAO;
    }

    /**
     * Take data from thread, convert to an entity and inserts into the database.
     */
    public void receiveData() {
        JsonLoader jsonLoader = new JsonLoader("Available Forces Receiver", link);
        Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
        try {
            JsonArray jsonArray = jsonArrayFuture.get();
            List<AvailableStopAndSearchesByForce> resultList = new ArrayList<>();
            for (Integer i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();
                JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                for (Integer z = 0; z < jsonToEntityConverter.convertToAvailableStopAndSearchesByForce(object).getForcesList().size(); z++) {
                    AvailableStopAndSearchesByForce availableForce = new AvailableStopAndSearchesByForce();
                    availableForce.setDate(jsonToEntityConverter.convertToAvailableStopAndSearchesByForce(object).getDate());
                    availableForce.addForceToList(jsonToEntityConverter.convertToAvailableStopAndSearchesByForce(object).getForcesList().get(z));
                    resultList.add(availableForce);
                }
            }
        stopAndSearchesByForceDAO.addAvailableForces(resultList);
        } catch (InterruptedException | ExecutionException e) {
            logger.warn("Interrupted | Execution exception in Json Loader: " + jsonLoader.getName());
        }
    }
}
