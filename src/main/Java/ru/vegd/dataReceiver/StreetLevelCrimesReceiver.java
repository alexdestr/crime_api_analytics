package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.Station;
import ru.vegd.linkBuilder.LinkBuider;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class StreetLevelCrimesReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StreetLevelCrimesReceiver.class.getName());

    private StreetLevelCrimesDao streetLevelCrimesDao;

    private String link;
    private List<Station> csvData;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    private List<JsonArray> resultList = new ArrayList<>();

    public StreetLevelCrimesReceiver(String link, List<Station> csvData, StreetLevelCrimesDao streetLevelCrimesDao) {
        this.link = link;
        this.csvData = csvData;
        this.streetLevelCrimesDao = streetLevelCrimesDao;
    }

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
                    for (Integer i = 0; i < jsonArray.size(); i++) {
                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                        JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                        streetLevelCrimesDao.add(jsonToEntityConverter.convertToStreetLevelCrimes(object));
                    }
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

