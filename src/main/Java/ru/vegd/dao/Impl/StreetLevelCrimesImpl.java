package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.entity.StreetLevelCrimes;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StreetLevelCrimesImpl implements StreetLevelCrimesDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    List list = new ArrayList<>();

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
                String.valueOf(crime.getCategory()),
                String.valueOf(crime.getLocatonType()),
                String.valueOf(crime.getLatitude()),
                String.valueOf(crime.getLongitude()),
                String.valueOf(crime.getStreetId()),
                String.valueOf(crime.getStreetName()),
                String.valueOf(crime.getContext()),
                String.valueOf(crime.getOutcomeCategory()),
                String.valueOf(crime.getOutcomeDate()),
                String.valueOf(crime.getPersistentId()),
                String.valueOf(crime.getId()),
                String.valueOf(crime.getLocationSubtype()),
                String.valueOf(crime.getMonth())
        );
    }

    @Override
    public void getCrime(Long id) {

    }

    

    @Override
    public void deleteCrime(Long id) {

    }
}
