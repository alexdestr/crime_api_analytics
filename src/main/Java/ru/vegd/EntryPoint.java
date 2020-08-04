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
            YearMonth fromDate = null;
            YearMonth toDate = null;

            if (optionsMap.get("startDate") != null && optionsMap.get("endDate") != null) {
                fromDate = YearMonth.parse((String) optionsMap.get("startDate"));
                toDate = YearMonth.parse((String) optionsMap.get("endDate"));
            }

            if (optionsMap.get("type").equals("downloadData")) {
                if (optionsMap.get("downloadData").equals("crimeCategories")) {
                    CrimeCategoriesReceiver crimeCategoriesReceiver = new CrimeCategoriesReceiver(csvData, crimeCategoriesDAO);
                    crimeCategoriesReceiver.receiveData();
                }

                if (optionsMap.get("downloadData").equals("forcesList")) {
                    ForcesListReceiver forcesListReceiver = new ForcesListReceiver(csvData, forcesListDAO);
                    forcesListReceiver.receiveData();
                }

                if (optionsMap.get("downloadData").equals("streetLevelCrimes")) {
                    StreetLevelCrimesReceiver streetLevelCrimesReceiver = new StreetLevelCrimesReceiver(csvData, streetLevelCrimesDAO);
                    streetLevelCrimesReceiver.receiveData(fromDate, toDate);
                }

                if (optionsMap.get("stopAndSearchesByForce").equals("stopAndSearchesByForce")) {
                    StopAndSearchesByForceReceiver stopAndSearchesByForceReceiver = new StopAndSearchesByForceReceiver(stopAndSearchesByForceDAO, forcesListDAO);
                    stopAndSearchesByForceReceiver.receiveData(fromDate, toDate);
                }
            }

            if (optionsMap.get("type").equals("receiveData")) {
                if (optionsMap.get("row").equals("1")) {
                    // Row #1
                    streetLevelCrimesDAO.getMostDangerousStreets(fromDate, toDate);
                }

                if (optionsMap.get("row").equals("2")) {
                    //Row #2
                    streetLevelCrimesDAO.getMonthToMonthCrimeVolumeComparison(fromDate, toDate);
                }

                if (optionsMap.get("row").equals("3")) {
                    //Row #3
                    streetLevelCrimesDAO.getCrimesWithSpecifiedOutcomeStatus("Investigation complete; no suspect identified", fromDate, toDate);
                }

                if (optionsMap.get("row").equals("4")) {
                    //Row #4
                    stopAndSearchesByForceDAO.getStatisticByEthnicity(fromDate, toDate);
                }

                if (optionsMap.get("row").equals("5")) {
                    //Row #5
                    stopAndSearchesByForceDAO.getMostProbableStopAndSearchSnapshotOnStreetLevel(fromDate, toDate);
                }

                if (optionsMap.get("row").equals("6")) {
                    //Row #6
                    commonRows.comparsionStopAndSearchesWithStreetLevelCrimes(fromDate, toDate);
                }
            }

        } catch (Exception e) {
            logger.error("Something went wrong.");
            e.printStackTrace();
        }
    }
}
