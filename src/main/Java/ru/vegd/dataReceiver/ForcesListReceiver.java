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

public class ForcesListReceiver {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ForcesListReceiver.class.getName());

    private static final Integer threadNum = Runtime.getRuntime().availableProcessors() + 1; // optimal number of threads

    private ForcesListDao forcesListDao;

    private String link;
    private List<Station> csvData;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
    private List<Force> resultList = new ArrayList<>();

    public ForcesListReceiver(String link, List<Station> csvData, ForcesListDao forcesListDao) {
        this.link = link;
        this.csvData = csvData;
        this.forcesListDao = forcesListDao;
    }

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
