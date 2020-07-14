package ru.vegd.utils;

import com.opencsv.CSVReader;
import ru.vegd.entity.Station;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CSVParser.class.getName());

    private static final String FILE_PATH = "LondonStations.csv";

    public static List<Station> getStations() {
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
        return csvData;
    }
}
