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
        String type = null;
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
            type = properties.getProperty("type");
            startDate = YearMonth.parse(properties.getProperty("startDate"));
            endDate = YearMonth.parse(properties.getProperty("endDate"));
        }

        entryPoint.entry();
    }

}
