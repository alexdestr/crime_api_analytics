package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StreetLevelCrimesDAO;
import ru.vegd.entity.StreetLevelCrime;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StreetLevelCrimesImpl implements StreetLevelCrimesDAO {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StreetLevelCrimesImpl.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_CRIME = "INSERT INTO streetLevelCrimes" +
            "(category, " +
            "location_type, " +
            "latitude, " +
            "longitude, " +
            "street_id, " +
            "street_name, " +
            "context, " +
            "outcome_category, " +
            "outcome_date, " +
            "persistent_id, " +
            "id, " +
            "location_subtype, " +
            "month) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT(id) DO UPDATE " +
            "SET category = ?, " +
            "location_type = ?, " +
            "latitude = ?, " +
            "longitude = ?, " +
            "street_id = ?, " +
            "street_name = ?, " +
            "context = ?, " +
            "outcome_category = ?, " +
            "outcome_date = ?, " +
            "persistent_id = ?, " +
            "id = ?, " +
            "location_subtype = ?, " +
            "month = ?";

    @Override
    public void add(List<StreetLevelCrime> crimeList) {
        jdbcTemplate.batchUpdate(SQL_ADD_CRIME,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, crimeList.get(i).getCategory());
                        ps.setString(2, crimeList.get(i).getLocatonType());
                        ps.setDouble(3, crimeList.get(i).getLatitude());
                        ps.setDouble(4, crimeList.get(i).getLongitude());
                        ps.setLong(5, crimeList.get(i).getStreetId());
                        ps.setString(6, crimeList.get(i).getStreetName());
                        ps.setString(7, crimeList.get(i).getContext());
                        ps.setString(8, crimeList.get(i).getOutcomeCategory());
                        ps.setString(9, String.valueOf(crimeList.get(i).getOutcomeDate()));
                        ps.setString(10, crimeList.get(i).getPersistentId());
                        ps.setLong(11, crimeList.get(i).getId());
                        ps.setString(12, crimeList.get(i).getLocatonType());
                        ps.setString(13, String.valueOf(crimeList.get(i).getMonth()));
                        ps.setString(14, crimeList.get(i).getCategory());
                        ps.setString(15, crimeList.get(i).getLocatonType());
                        ps.setDouble(16, crimeList.get(i).getLatitude());
                        ps.setDouble(17, crimeList.get(i).getLongitude());
                        ps.setLong(18, crimeList.get(i).getStreetId());
                        ps.setString(19, crimeList.get(i).getStreetName());
                        ps.setString(20, crimeList.get(i).getContext());
                        ps.setString(21, crimeList.get(i).getOutcomeCategory());
                        ps.setString(22, String.valueOf(crimeList.get(i).getOutcomeDate()));
                        ps.setString(23, crimeList.get(i).getPersistentId());
                        ps.setLong(24, crimeList.get(i).getId());
                        ps.setString(25, crimeList.get(i).getLocationSubtype());
                        ps.setString(26, String.valueOf(crimeList.get(i).getMonth()));
                    }

                    @Override
                    public int getBatchSize() {
                        return crimeList.size();
                    }
                }
        );
    }
}
