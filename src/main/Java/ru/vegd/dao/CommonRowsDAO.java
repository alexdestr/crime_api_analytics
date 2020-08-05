package ru.vegd.dao;

import java.time.YearMonth;
import java.util.List;

public interface CommonRowsDAO {
    List<String> comparsionStopAndSearchesWithStreetLevelCrimes(YearMonth from, YearMonth to);
}
