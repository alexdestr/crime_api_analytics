package ru.vegd.dao;

import java.time.YearMonth;

public interface CommonRowsDAO {
    void comparsionStopAndSearchesWithStreetLevelCrimes(YearMonth from, YearMonth to);
}
