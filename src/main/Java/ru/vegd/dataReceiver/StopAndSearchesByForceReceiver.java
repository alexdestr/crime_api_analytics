package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.dataReceiver.loader.JsonLoader;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.Force;
import ru.vegd.entity.Station;
import ru.vegd.entity.StopAndSearchesByForce;
import ru.vegd.linkBuilder.StopAndSearchesByForceLinkBuilder;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class StopAndSearchesByForceReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StopAndSearchesByForceReceiver.class.getName());

    private static final Integer threadNum = Runtime.getRuntime().availableProcessors() + 1; // optimal number of threads
    private static final String link = "https://data.police.uk/api/stops-force";

    private StopAndSearchesByForceDAO stopAndSearchesByForceDAO;
    private ForcesListDAO forcesListDAO;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<JsonArray> resultList = new ArrayList<>();

    List<Force> forceList;

    /**
     * @param stopAndSearchesByForceDAO DAO with injected connection to load data into a database.
     */
    public StopAndSearchesByForceReceiver(StopAndSearchesByForceDAO stopAndSearchesByForceDAO, ForcesListDAO forcesListDAO) {
        this.stopAndSearchesByForceDAO = stopAndSearchesByForceDAO;
        this.forcesListDAO = forcesListDAO;
    }

    public void receiveData(String force, YearMonth fromDate, YearMonth toDate) {
        forceList.addAll(forcesListDAO.getAllForces());
        for (Force police : forceList) {
            for (YearMonth date = fromDate; !date.equals(toDate.plusMonths(1L)); date = date.plusMonths(1L)) {
                String finalLink = new StopAndSearchesByForceLinkBuilder().setStartLink(link)
                        .setForce(police.getId())
                        .setDate(date)
                        .build();
                JsonLoader jsonLoader = new JsonLoader("JsonLoader" + date.toString() + " Street: " + police.getId(), finalLink);
                Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
                try {
                    JsonArray jsonArray = jsonArrayFuture.get();
                    List<StopAndSearchesByForce> resultList = new ArrayList<>();
                    for (Integer i = 0; i < jsonArray.size(); i++) {
                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                        JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                        resultList.add(jsonToEntityConverter.(object));
                    }
                    streetLevelCrimesDAO.add(resultList);
                } catch (InterruptedException e) {
                    logger.warn("Thread interrupted!");
                } catch (ExecutionException e) {
                    logger.warn("Execution interrupted!");
                }
            }
        }
        try {
            executor.shutdown();
            final boolean done = executor.awaitTermination(csvData.size() * 11, TimeUnit.SECONDS);
            if (!done) {
                logger.warn("Not all data has received");
            }
        } catch (InterruptedException e) {
            logger.warn("Some threads are closed ahead of schedule");
        }
    }


}
