package ru.vegd.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDAO;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.dao.impl.CommonRowsImpl;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.dao.StreetLevelCrimesDAO;
import ru.vegd.receiver.*;
import ru.vegd.entity.Station;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Component
public class ArgumentParser {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ArgumentParser.class.getName());

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
        BufferedWriter writer = null;
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

                if (optionsMap.get("downloadData").equals("stopAndSearchesByForce")) {
                    StopAndSearchesAvailableForcesReceiver stopAndSearchesAvailableForcesReceiver = new StopAndSearchesAvailableForcesReceiver(stopAndSearchesByForceDAO);
                    stopAndSearchesAvailableForcesReceiver.receiveData();
                    StopAndSearchesByForceReceiver stopAndSearchesByForceReceiver = new StopAndSearchesByForceReceiver(stopAndSearchesByForceDAO, forcesListDAO);
                    stopAndSearchesByForceReceiver.receiveData(fromDate, toDate);
                }

                if (optionsMap.get("downloadData").equals("updateAvailableList")) {
                    StopAndSearchesAvailableForcesReceiver stopAndSearchesAvailableForcesReceiver = new StopAndSearchesAvailableForcesReceiver(stopAndSearchesByForceDAO);
                    stopAndSearchesAvailableForcesReceiver.receiveData();
                }
            }

            if (optionsMap.get("type").equals("receiveData")) {
                if (optionsMap.get("row").equals("1")) {
                    // Row #1
                    File file = new File("programOutputRow#1.log");
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.flush();
                    for (String str : streetLevelCrimesDAO.getMostDangerousStreets(fromDate, toDate)) {
                        System.out.println(str);
                        writer.write(str);
                        writer.newLine();
                    }
                }

                if (optionsMap.get("row").equals("2")) {
                    //Row #2
                    File file = new File("programOutputRow#2.log");
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.flush();
                    for (String str : streetLevelCrimesDAO.getMonthToMonthCrimeVolumeComparison(fromDate, toDate)) {
                        System.out.println(str);
                        writer.write(str);
                        writer.newLine();
                    }
                }

                if (optionsMap.get("row").equals("3")) {
                    //Row #3
                    File file = new File("programOutputRow#3.log");
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.flush();
                    for (String str : streetLevelCrimesDAO.getCrimesWithSpecifiedOutcomeStatus("Investigation complete; no suspect identified", fromDate, toDate)) {
                        System.out.println(str);
                        writer.write(str);
                        writer.newLine();
                    }
                }

                if (optionsMap.get("row").equals("4")) {
                    //Row #4
                    File file = new File("programOutputRow#4.log");
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.flush();
                    for (String str : stopAndSearchesByForceDAO.getStatisticByEthnicity(fromDate, toDate)) {
                        System.out.println(str);
                        writer.write(str);
                        writer.newLine();
                    }
                }

                if (optionsMap.get("row").equals("5")) {
                    //Row #5
                    File file = new File("programOutputRow#5.log");
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.flush();
                    for (String str : stopAndSearchesByForceDAO.getMostProbableStopAndSearchSnapshotOnStreetLevel(fromDate, toDate)) {
                        System.out.println(str);
                        writer.write(str);
                        writer.newLine();
                    }
                }

                if (optionsMap.get("row").equals("6")) {
                    //Row #6
                    File file = new File("programOutputRow#6.log");
                    writer = new BufferedWriter(new FileWriter(file));
                    writer.flush();
                    for (String str : commonRows.comparsionStopAndSearchesWithStreetLevelCrimes(fromDate, toDate)) {
                        System.out.println(str);
                        writer.write(str);
                        writer.newLine();
                    }
                }
                if (writer != null) {
                    writer.close();
                }
            }

        } catch (Exception e) {
            logger.error("Something went wrong: " + e.getMessage());
        }
    }
}
