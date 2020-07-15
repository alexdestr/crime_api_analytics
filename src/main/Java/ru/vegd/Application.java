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
import ru.vegd.utils.CSVParser;

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

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);

        ApplicationContext ctx =
                new AnnotationConfigApplicationContext("ru.vegd");
        EntryPoint entryPoint = (EntryPoint) ctx.getBean("entryPoint");

        Options options = new Options();
        CommandLine cmd = null;
        String link = null;
        String lng = null;
        String lat = null;
        YearMonth startDate = null;
        YearMonth endDate = null;

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
        }

        if (Objects.requireNonNull(cmd).hasOption("D")) {
            Properties properties = cmd.getOptionProperties("D");
            link = properties.getProperty("link");
            startDate = YearMonth.parse(properties.getProperty("startDate"));
            endDate = YearMonth.parse(properties.getProperty("endDate"));
        }

        entryPoint.entry();

    }

}
