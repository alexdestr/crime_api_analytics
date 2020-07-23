package ru.vegd.dao;

import ru.vegd.entity.StopAndSearchesByForce;

import java.time.YearMonth;
import java.util.List;

public interface StopAndSearchesByForceDAO {
    void add(List<StopAndSearchesByForce> stopAndSearchesByForce);
}
