package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.loader.JsonLoader;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.Station;
import ru.vegd.entity.StreetLevelCrime;
import ru.vegd.linkBuilder.LinkBuider;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Processes data and inserts into the database.
 */
public class StreetLevelCrimesReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StreetLevelCrimesReceiver.class.getName());

    private static final Integer threadNum = Runtime.getRuntime().availableProcessors() + 1; // optimal number of threads
    private static final String link = "https://data.police.uk/api/crimes-street/all-crime";

    private StreetLevelCrimesDao streetLevelCrimesDao;

    private List<Station> csvData;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<JsonArray> resultList = new ArrayList<>();

    /**
     * @param csvData list of police stations and their coordinates.
     * @param streetLevelCrimesDao DAO with injected connection to load data into a database.
     */
    public StreetLevelCrimesReceiver(List<Station> csvData, StreetLevelCrimesDao streetLevelCrimesDao) {
        this.csvData = csvData;
        this.streetLevelCrimesDao = streetLevelCrimesDao;
    }

    /**
     * Take data from thread, convert to an entity and inserts into the database.
     * @param fromDate date in the format (YYYY-MM) from which data will be received (inclusively)
     * @param toDate date in the format (YYYY-MM) for which data will be received (inclusively)
     */
    public void receiveData(YearMonth fromDate, YearMonth toDate) {
        for (Station station : csvData) {
            for (YearMonth date = fromDate; !date.equals(toDate.plusMonths(1L)); date = date.plusMonths(1L)) {
                String finalLink = new LinkBuider().setStartLink(link)
                        .setLongitude(station.getLongitude())
                        .setLatitude(station.getLatitude())
                        .setDate(date)
                        .build();
                JsonLoader jsonLoader = new JsonLoader("JsonLoader" + date.toString() + " Street: " + station.getStreetName(), finalLink);
                Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
                try {
                    JsonArray jsonArray = jsonArrayFuture.get();
                    List<StreetLevelCrime> resultList = new ArrayList<>();
                    for (Integer i = 0; i < jsonArray.size(); i++) {
                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                        JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                        resultList.add(jsonToEntityConverter.convertToStreetLevelCrimes(object));
                    }
                    streetLevelCrimesDao.add(resultList);
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

