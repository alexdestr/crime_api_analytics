package ru.vegd.receiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.receiver.loader.JsonLoader;
import ru.vegd.receiver.utils.JsonToEntityConverter;
import ru.vegd.entity.Force;
import ru.vegd.entity.StopAndSearchesByForce;
import ru.vegd.builder.StopAndSearchesByForceLinkBuilder;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Processes data and inserts into the database.
 */
public class StopAndSearchesByForceReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StopAndSearchesByForceReceiver.class.getName());

    private static final Integer threadNum = Runtime.getRuntime().availableProcessors() + 1; // optimal number of threads
    private static final String link = "https://data.police.uk/api/stops-force";

    private StopAndSearchesByForceDAO stopAndSearchesByForceDAO;
    private ForcesListDAO forcesListDAO;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<JsonArray> resultList = new ArrayList<>();

    private List<Force> forceList = new ArrayList<>();

    /**
     * @param stopAndSearchesByForceDAO DAO with injected connection to load data into a database.
     * @param forcesListDAO             DAO with injected connection to load data into a database.
     */
    public StopAndSearchesByForceReceiver(StopAndSearchesByForceDAO stopAndSearchesByForceDAO, ForcesListDAO forcesListDAO) {
        this.stopAndSearchesByForceDAO = stopAndSearchesByForceDAO;
        this.forcesListDAO = forcesListDAO;
    }

    /**
     * Take data from thread, convert to an entity and inserts into the database.
     *
     * @param fromDate date in the format (YYYY-MM) from which data will be received (inclusively)
     * @param toDate   date in the format (YYYY-MM) for which data will be received (inclusively)
     */
    public void receiveData(YearMonth fromDate, YearMonth toDate) {
        forceList.addAll(forcesListDAO.getAllForces());
        for (Force police : forceList) {
            for (YearMonth date = fromDate; !date.equals(toDate.plusMonths(1L)); date = date.plusMonths(1L)) {
                String finalLink = new StopAndSearchesByForceLinkBuilder().setStartLink(link)
                        .setForce(police.getId())
                        .setDate(date)
                        .setStopAndSearchesDAO(stopAndSearchesByForceDAO)
                        .build();
                if (finalLink != null) {
                    JsonLoader jsonLoader = new JsonLoader("JsonLoader" + date.toString() + " Street: " + police.getId(), finalLink);
                    Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
                    try {
                        JsonArray jsonArray = jsonArrayFuture.get();
                        List<StopAndSearchesByForce> resultList = new ArrayList<>();
                        for (Integer i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();
                            JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                            resultList.add(jsonToEntityConverter.convertToStopAndSearchByForce(object));
                        }
                        stopAndSearchesByForceDAO.add(resultList);
                    } catch (InterruptedException e) {
                        logger.warn("Thread interrupted in Json Loader: " + jsonLoader.getName());
                    } catch (ExecutionException e) {
                        logger.warn("Execution interrupted in JsonLoader: " + jsonLoader.getName());
                    }
                }
            }
        }
        try {
            executor.shutdown();
            final boolean done = executor.awaitTermination(forceList.size() * 11, TimeUnit.SECONDS);
            if (!done) {
                logger.warn("Not all data has received");
            }
        } catch (InterruptedException e) {
            logger.warn("Some threads are closed ahead of schedule");
        }
    }


}
