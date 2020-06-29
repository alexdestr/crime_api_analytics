package ru.vegd;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDao;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.Receiver;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;

import java.time.YearMonth;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
@Component
public class Application {

    @Autowired
    CrimeCategoriesDao crimeCategoriesDao;

    @Autowired
    StreetLevelCrimesDao streetLevelCrimesDao;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext("ru.vegd");

        Application application = ctx.getBean(Application.class);
        CrimeCategoriesDao crimeCategoriesDao = application.crimeCategoriesDao;
        StreetLevelCrimesDao streetLevelCrimesDao = application.streetLevelCrimesDao;


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
        YearMonth toDate = YearMonth.of(2018, 4);

        Receiver crimesCategoryReceiver = new Receiver("https://data.police.uk/api/crime-categories");
        Receiver streetLevelCrimesReceiver = new Receiver(link);
        List<JsonArray> crimesCategories = crimesCategoryReceiver.receiveData();
        List<JsonArray> jsonArrayList = streetLevelCrimesReceiver.receiveData(fromDate, toDate);

        for (Integer id = 0; id < crimesCategories.size(); id++) {
            JsonArray jsonArray = crimesCategories.get(id);
            for (Integer z = 0; z < jsonArray.size(); z++) {
                JsonObject json = jsonArray.get(z).getAsJsonObject();
                JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                crimeCategoriesDao.addCrimeCategory(jsonToEntityConverter.convertToCrimeCategories(json));
            }
        }

        for (Integer id = 0; id < jsonArrayList.size(); id++) {
            JsonArray jsonArray = jsonArrayList.get(id);
            for (Integer z = 0; z < jsonArray.size(); z++) {
                JsonObject json = jsonArray.get(z).getAsJsonObject();
                JsonToEntityConverter jsonToEntityConverter = new JsonToEntityConverter();
                streetLevelCrimesDao.addCrime(jsonToEntityConverter.convertToStreetLevelCrimes(json));
            }
        }
    }

}
