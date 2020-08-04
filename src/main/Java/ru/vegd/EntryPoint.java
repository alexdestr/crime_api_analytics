package ru.vegd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDAO;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.dao.Impl.CommonRowsImpl;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.dao.StreetLevelCrimesDAO;
import ru.vegd.dataReceiver.CrimeCategoriesReceiver;
import ru.vegd.dataReceiver.ForcesListReceiver;
import ru.vegd.dataReceiver.StopAndSearchesByForceReceiver;
import ru.vegd.dataReceiver.StreetLevelCrimesReceiver;
import ru.vegd.entity.Station;
import ru.vegd.utils.CSVParser;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EntryPoint {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EntryPoint.class.getName());

    @Autowired
    private CrimeCategoriesDAO crimeCategoriesDAO;

    @Autowired
    private ForcesListDAO forcesListDAO;

    @Autowired
    private StreetLevelCrimesDAO streetLevelCrimesDAO;

    @Autowired
    private StopAndSearchesByForceDAO stopAndSearchesByForceDAO;

    @Autowired
    private CommonRowsImpl commonRows;

    public void entry(Map optionsMap) {
        try {
            List<Station> csvData = CSVParser.getStations();

            YearMonth fromDate = YearMonth.parse(optionsMap.get("fromDate"));
            YearMonth toDate = YearMonth.parse(optionsMap.get("toDate"));

            CrimeCategoriesReceiver crimeCategoriesReceiver = new CrimeCategoriesReceiver(csvData, crimeCategoriesDAO);
            //crimeCategoriesReceiver.receiveData();

            ForcesListReceiver forcesListReceiver = new ForcesListReceiver(csvData, forcesListDAO);
            //forcesListReceiver.receiveData();

            StreetLevelCrimesReceiver streetLevelCrimesReceiver = new StreetLevelCrimesReceiver(csvData, streetLevelCrimesDAO);
            //streetLevelCrimesReceiver.receiveData(fromDate, toDate);

            StopAndSearchesByForceReceiver stopAndSearchesByForceReceiver = new StopAndSearchesByForceReceiver(stopAndSearchesByForceDAO, forcesListDAO);
            //stopAndSearchesByForceReceiver.receiveData(fromDate, toDate);

            // Row #1
            //streetLevelCrimesDAO.getMostDangerousStreets(fromDate, toDate);

            //Row #2
            //streetLevelCrimesDAO.getMonthToMonthCrimeVolumeComparison(fromDate, toDate);

            //Row #3
            //streetLevelCrimesDAO.getCrimesWithSpecifiedOutcomeStatus("Investigation complete; no suspect identified", fromDate, toDate);

            //Row #4
            //stopAndSearchesByForceDAO.getStatisticByEthnicity(fromDate, toDate);

            //Row #5
            //stopAndSearchesByForceDAO.getMostProbableStopAndSearchSnapshotOnStreetLevel(fromDate, toDate);

            //Row #6
            //commonRows.comparsionStopAndSearchesWithStreetLevelCrimes(fromDate, toDate);

        } catch (Exception e) {
            logger.error("Something went wrong.");
            e.printStackTrace();
        }
    }
}
