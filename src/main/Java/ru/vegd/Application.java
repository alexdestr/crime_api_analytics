package ru.vegd;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.Receiver;
import ru.vegd.entity.StreetLevelCrimes;

import java.time.YearMonth;
import java.util.*;

@SpringBootApplication
@Component
public class Application {

    @Autowired
    static StreetLevelCrimesDao streetLevelCrimesDao;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);

        Options options = new Options();
        CommandLine cmd = null;
        String link = "https://data.police.uk/api/crimes-street/all-crime?lng=-0.242033&lat=51.467128&date=2018-01";
        String lng = null;
        String lat = null;
        String date = null;

        Option propertyOption = Option.builder()
                .longOpt("C")
                .argName("property=value")
                .hasArg()
                .valueSeparator()
                .build();
        options.addOption(propertyOption);

        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cmd.hasOption("D")) {
            Properties properties = cmd.getOptionProperties("D");
            link = properties.getProperty("link");
            lng = properties.getProperty("lng");
            lat = properties.getProperty("lat");
            date = properties.getProperty("date");
        }

        YearMonth fromDate = YearMonth.of(2018, 1);
        YearMonth toDate = YearMonth.of(2018, 3);

        Receiver receiver = new Receiver(link);
        List<JsonArray> jsonArrayList = receiver.getData(fromDate, toDate);

        for (Integer id = 0; id < jsonArrayList.size(); id++) {
            JsonArray jsonArray = jsonArrayList.get(id);
            for (Integer z = 0; z < jsonArray.size(); z++) {
                JsonObject json = jsonArray.get(z).getAsJsonObject();
                StreetLevelCrimes crimes = new StreetLevelCrimes(json.get("category").getAsString(),
                        json.get("location_type").getAsString(),
                        getValuesFromMap((Map) json.get("location")),
                        Long.valueOf(((Map) ((Map) json.get("location")).get("street")).get("id").toString()),
                        ((Map) ((Map) json.get("location")).get("street")).get("id").toString(),
                        json.get("context").getAsLong(),
                        json.get("outcome_status").getAsString(),
                        json.get("persistent_id").getAsString(),
                        json.get("id").getAsLong(),
                        json.get("location_subtype").getAsString(),
                        YearMonth.parse(json.get("date").getAsString())
                );
                streetLevelCrimesDao.addCrime(crimes);
            }
        }
    }
        private static List<Double> getValuesFromMap(Map map) {
            Iterator<Map.Entry> itr = map.entrySet().iterator();
            List<Double> values = new ArrayList();
            while (itr.hasNext()) {
                Map.Entry pair = itr.next();
                values.add((Double) pair.getValue());
            }
            return values;
        }
}
