package ru.vegd;

import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDao;
import ru.vegd.dao.ForcesListDao;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.dataReceiver.CrimeCategoriesReceiver;
import ru.vegd.dataReceiver.ForcesListReceiver;
import ru.vegd.dataReceiver.StreetLevelCrimesReceiver;
import ru.vegd.entity.Station;
import ru.vegd.utils.CSVParser;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Component
public class EntryPoint {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EntryPoint.class.getName());

    @Autowired
    private CrimeCategoriesDao crimeCategoriesDao;

    @Autowired
    private ForcesListDao forcesListDao;

    @Autowired
    private StreetLevelCrimesDao streetLevelCrimesDao;

    public void entry() {
        try {
            List<Station> csvData = CSVParser.getStations();

            YearMonth fromDate = YearMonth.of(2018, 1);
            YearMonth toDate = YearMonth.of(2018, 6);

            CrimeCategoriesReceiver crimeCategoriesReceiver = new CrimeCategoriesReceiver(csvData, crimeCategoriesDao);
            crimeCategoriesReceiver.receiveData();

            ForcesListReceiver forcesListReceiver = new ForcesListReceiver(csvData, forcesListDao);
            forcesListReceiver.receiveData();

            StreetLevelCrimesReceiver streetLevelCrimesReceiver = new StreetLevelCrimesReceiver(csvData, streetLevelCrimesDao);
            streetLevelCrimesReceiver.receiveData(fromDate, toDate);
        } catch (Exception e) {
            logger.error("Something went wrong.");
        }
    }
}
