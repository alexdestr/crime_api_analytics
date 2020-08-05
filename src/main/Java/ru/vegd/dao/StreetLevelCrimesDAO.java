package ru.vegd.dao;

import ru.vegd.entity.StreetLevelCrime;

import java.time.YearMonth;
import java.util.List;

public interface StreetLevelCrimesDAO {
    void add(List<StreetLevelCrime> crimeList);
    List<String> getMostDangerousStreets(YearMonth from, YearMonth to);
    List<String> getMonthToMonthCrimeVolumeComparison(YearMonth from, YearMonth to);
    List<String> getCrimesWithSpecifiedOutcomeStatus(String outcomeCategory, YearMonth from, YearMonth to);
}
