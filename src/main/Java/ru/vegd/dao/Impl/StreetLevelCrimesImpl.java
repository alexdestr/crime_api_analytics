package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.entity.StreetLevelCrimes;

@Repository
public class StreetLevelCrimesImpl implements StreetLevelCrimesDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_CRIME = "INSERT INTO streetLevelCrimes" +
            "(category, " +
            "locationType, " +
            "latitude, " +
            "longitude, " +
            "streetId, " +
            "streetName, " +
            "context, " +
            "outcomeCategory, " +
            "outcomeDate, " +
            "persistentId, " +
            "id, " +
            "locationSubtype, " +
            "month) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public void addCrime(StreetLevelCrimes crime) {
        jdbcTemplate.batchUpdate(SQL_ADD_CRIME,
                crime.getCategory().toString(),
                crime.getLocatonType().toString(),
                crime.getLatitude().toString(),
                crime.getLongitude().toString(),
                crime.getStreetId().toString(),
                crime.getStreetName().toString(),
                crime.getContext().toString(),
                crime.getOutcomeCategory().toString(),
                crime.getOutcomeDate().toString(),
                crime.getPersistentId().toString(),
                crime.getId().toString(),
                crime.getLocationSubtype().toString(),
                crime.getMonth().toString());
    }

    @Override
    public void getCrime(Long id) {

    }

    @Override
    public void deleteCrime(Long id) {

    }
}
