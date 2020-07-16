package ru.vegd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vegd.dao.CrimeCategoriesDAO;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.dao.StreetLevelCrimesDAO;
import ru.vegd.dataReceiver.CrimeCategoriesReceiver;
import ru.vegd.dataReceiver.ForcesListReceiver;
import ru.vegd.dataReceiver.StopAndSearchesByForceReceiver;
import ru.vegd.dataReceiver.StreetLevelCrimesReceiver;
import ru.vegd.entity.Station;
import ru.vegd.utils.CSVParser;

import java.time.YearMonth;
import java.util.List;

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

    public void entry() {
        try {
            List<Station> csvData = CSVParser.getStations();

            YearMonth fromDate = YearMonth.of(2018, 1);
            YearMonth toDate = YearMonth.of(2018, 6);

            CrimeCategoriesReceiver crimeCategoriesReceiver = new CrimeCategoriesReceiver(csvData, crimeCategoriesDAO);
            //crimeCategoriesReceiver.receiveData();

            ForcesListReceiver forcesListReceiver = new ForcesListReceiver(csvData, forcesListDAO);
            //forcesListReceiver.receiveData();

            StreetLevelCrimesReceiver streetLevelCrimesReceiver = new StreetLevelCrimesReceiver(csvData, streetLevelCrimesDAO);
            //streetLevelCrimesReceiver.receiveData(fromDate, toDate);

            StopAndSearchesByForceReceiver stopAndSearchesByForceReceiver = new StopAndSearchesByForceReceiver(stopAndSearchesByForceDAO, forcesListDAO);
            stopAndSearchesByForceReceiver.receiveData(fromDate, toDate);
        } catch (Exception e) {
            logger.error("Something went wrong.");
            e.printStackTrace();
        }
    }
}
