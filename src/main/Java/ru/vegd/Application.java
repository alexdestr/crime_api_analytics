package ru.vegd;

import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDAO;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.dao.StreetLevelCrimesDAO;

import java.time.YearMonth;
import java.util.*;

@SpringBootApplication
@Component
public class Application {

    @Autowired
    CrimeCategoriesDAO crimeCategoriesDAO;

    @Autowired
    ForcesListDAO forcesListDAO;

    @Autowired
    StreetLevelCrimesDAO streetLevelCrimesDAO;

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
        Map<String, String> optionsMap = new HashMap<>();

        Option propertyOption = Option.builder()
                .longOpt("D")
                .argName("property=value" )
                .hasArgs()
                .valueSeparator()
                .numberOfArgs(2)
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
            optionsMap.put("type", properties.getProperty("type"));
            optionsMap.put("downloadData", properties.getProperty("downloadData"));
            optionsMap.put("row", properties.getProperty("row"));
            optionsMap.put("startDate", properties.getProperty("startDate"));
            optionsMap.put("endDate", properties.getProperty("endDate"));
            optionsMap.put("lng", properties.getProperty("lng"));
            optionsMap.put("lat", properties.getProperty("lat"));
        }

        entryPoint.entry(optionsMap);
    }
}
