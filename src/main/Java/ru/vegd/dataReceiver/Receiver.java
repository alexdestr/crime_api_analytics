package ru.vegd.dataReceiver;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.StreetLocation;
import ru.vegd.linkBuilder.LinkBuider;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Receiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Receiver.class.getName());

    private StreetLevelCrimesDao streetLevelCrimesDao;

    private String link;
    private List<StreetLocation> csvData;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    private List<JsonArray> resultList = new ArrayList<>();

    public Receiver(String link, List<StreetLocation> csvData, StreetLevelCrimesDao streetLevelCrimesDao) {
        this.link = link;
        this.csvData = csvData;
        this.streetLevelCrimesDao = streetLevelCrimesDao;
    }

    public void receiveData(YearMonth fromDate, YearMonth toDate) {
        for (StreetLocation streetLocation : csvData) {
            for (YearMonth date = fromDate; !date.equals(toDate.plusMonths(1L)); date = date.plusMonths(1L)) {
                String finalLink = new LinkBuider().setStartLink(link)
                        .setLongitude(streetLocation.getLongitude())
                        .setLatitude(streetLocation.getLatitude())
                        .setDate(date)
                        .build();
                JsonLoader jsonLoader = new JsonLoader("JsonLoader" + date.toString() + " Street: " + streetLocation.getStreetName(), finalLink);
                Future<JsonArray> jsonArrayFuture = executor.submit(jsonLoader);
                try {
                    JsonArray jsonArray = jsonArrayFuture.get();
                    for (Integer i = 0; i < jsonArray.size(); i++) {
                        JsonObject object = jsonArray.get(i).getAsJsonObject();
                        JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                        streetLevelCrimesDao.addCrime(jsonToEntityConverter.convertToStreetLevelCrimes(object));
                    }
                } catch (InterruptedException e) {
                    logger.warn("Thread interrupted!");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    logger.warn("Execution interrupted!");
                    e.printStackTrace();
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
            logger.warn("Interrupt exception");
        }
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

