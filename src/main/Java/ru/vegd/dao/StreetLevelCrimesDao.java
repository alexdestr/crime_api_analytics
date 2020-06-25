package ru.vegd.dao;

import ru.vegd.entity.StreetLevelCrimes;

public interface StreetLevelCrimesDao {

    public void addCrime(StreetLevelCrimes crime);

    public void getCrime(Long id);

    public void deleteCrime(Long id);

}
