package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StreetLevelCrimesDao;
import ru.vegd.entity.StreetLevelCrimes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT(id) DO UPDATE " +
            "SET category = ?, " +
            "locationType = ?, " +
            "latitude = ?, " +
            "longitude = ?, " +
            "streetId = ?, " +
            "streetName = ?, " +
            "context = ?, " +
            "outcomeCategory = ?, " +
            "outcomeDate = ?, " +
            "persistentId = ?, " +
            "id = ?, " +
            "locationSubtype = ?, " +
            "month = ?";

    @Override
    public void addCrime(StreetLevelCrimes crime) {
        jdbcTemplate.batchUpdate(SQL_ADD_CRIME,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, crime.getCategory());
                        ps.setString(2, crime.getLocatonType());
                        ps.setDouble(3, crime.getLatitude());
                        ps.setDouble(4, crime.getLongitude());
                        ps.setLong(5, crime.getStreetId());
                        ps.setString(6, crime.getStreetName());
                        ps.setString(7, crime.getContext());
                        ps.setString(8, crime.getOutcomeCategory());
                        ps.setString(9, String.valueOf(crime.getOutcomeDate()));
                        ps.setString(10, crime.getPersistentId());
                        ps.setLong(11, crime.getId());
                        ps.setString(12, crime.getLocatonType());
                        ps.setString(13, String.valueOf(crime.getMonth()));
                        ps.setString(14, crime.getCategory());
                        ps.setString(15, crime.getLocatonType());
                        ps.setDouble(16, crime.getLatitude());
                        ps.setDouble(17, crime.getLongitude());
                        ps.setLong(18, crime.getStreetId());
                        ps.setString(19, crime.getStreetName());
                        ps.setString(20, crime.getContext());
                        ps.setString(21, crime.getOutcomeCategory());
                        ps.setString(22, String.valueOf(crime.getOutcomeDate()));
                        ps.setString(23, crime.getPersistentId());
                        ps.setLong(24, crime.getId());
                        ps.setString(25, crime.getLocationSubtype());
                        ps.setString(26, String.valueOf(crime.getMonth()));
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                }
        );
    }

    @Override
    public void getCrime(Long id) {

    }



    @Override
    public void deleteCrime(Long id) {

    }
}
