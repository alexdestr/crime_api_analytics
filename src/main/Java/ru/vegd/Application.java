package ru.vegd;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDao;
import ru.vegd.dao.ForcesListDao;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.CrimeCategoriesReceiver;
import ru.vegd.dataReceiver.ForcesListReceiver;
import ru.vegd.dataReceiver.StreetLevelCrimesReceiver;
import ru.vegd.dataReceiver.utils.JsonToEntityConverter;
import ru.vegd.entity.Station;

import java.io.FileReader;
import java.io.IOException;
import java.time.YearMonth;
import java.util.*;

@SpringBootApplication
@Component
public class Application {

    @Autowired
    CrimeCategoriesDao crimeCategoriesDao;

    @Autowired
    ForcesListDao forcesListDao;

    @Autowired
    StreetLevelCrimesDao streetLevelCrimesDao;

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Application.class.getName());

    private static final String FILE_PATH = "LondonStations.csv";

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext("ru.vegd");

        Application application = ctx.getBean(Application.class);
        CrimeCategoriesDao crimeCategoriesDao = application.crimeCategoriesDao;
        StreetLevelCrimesDao streetLevelCrimesDao = application.streetLevelCrimesDao;
        ForcesListDao forcesListDao = application.forcesListDao;

        CSVReader reader = null;
        List<Station> csvData = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(FILE_PATH));
            String[] line = null;
            while ((line = reader.readNext()) != null) {
                if (!line[0].equals("name")) {
                    csvData.add(new Station(line[0], Double.valueOf(line[1]), Double.valueOf(line[2])));
                }
            }
        } catch (IOException e) {
            logger.error("Can't open file");
        }

        Options options = new Options();
        CommandLine cmd = null;
        String link;
        String lng = null;
        String lat = null;
        String date = null;

        YearMonth fromDate = YearMonth.of(2018, 1);
        YearMonth toDate = YearMonth.of(2018, 6);

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
            logger.warn("Parsing cmd error.");
            e.printStackTrace();
        }

        if (Objects.requireNonNull(cmd).hasOption("D")) {
            Properties properties = cmd.getOptionProperties("D");
            link = properties.getProperty("link");
            date = properties.getProperty("date");
        }

        CrimeCategoriesReceiver crimeCategoriesReceiver = new CrimeCategoriesReceiver(csvData, crimeCategoriesDao);
        crimeCategoriesReceiver.receiveData();

        ForcesListReceiver forcesListReceiver = new ForcesListReceiver(csvData, forcesListDao);
        forcesListReceiver.receiveData();

        StreetLevelCrimesReceiver streetLevelCrimesReceiver = new StreetLevelCrimesReceiver(csvData, streetLevelCrimesDao);
        streetLevelCrimesReceiver.receiveData(fromDate, toDate);
    }

}
