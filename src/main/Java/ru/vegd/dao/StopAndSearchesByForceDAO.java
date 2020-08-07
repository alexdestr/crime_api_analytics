package ru.vegd.dao;

import ru.vegd.entity.AvailableStopAndSearchesByForce;
import ru.vegd.entity.StopAndSearchesByForce;

import java.time.YearMonth;
import java.util.List;

public interface StopAndSearchesByForceDAO {
    void add(List<StopAndSearchesByForce> stopAndSearchesByForce);
    void addAvailableForces(List<AvailableStopAndSearchesByForce> availableStopAndSearchesByForces);
    Boolean[] checkForAvailability(YearMonth date, String force);
    List<String> getStatisticByEthnicity(YearMonth from, YearMonth to);
    List<String> getMostProbableStopAndSearchSnapshotOnStreetLevel(YearMonth from, YearMonth to);
}
