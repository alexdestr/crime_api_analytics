package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.entity.StopAndSearchesByForce;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StopAndSearchesByForceImpl implements StopAndSearchesByForceDAO {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StopAndSearchesByForceImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_STOP_AND_SEARCHES_BY_FORCE = "INSERT INTO stopandsearchesbyforce " +
            "(type, " +
            "involved_person, " +
            "datetime, " +
            "operation, " +
            "operation_name, " +
            "latitude, " +
            "longitude, " +
            "street_id, " +
            "street_name, " +
            "gender, " +
            "age_range, " +
            "self_defined_ethnicity, " +
            "officer_defined_ethnicity, " +
            "legislation, " +
            "object_of_search, " +
            "outcome, " +
            "outcome_linked_to_object_of_search, " +
            "removal_of_more_than_outer_clothing) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON CONFLICT DO UPDATE " +
            "SET " +
            "(type = ?, " +
            "involved_person = ?, " +
            "datetime = ?, " +
            "operation = ?, " +
            "operation_name = ?, " +
            "latitude = ?, " +
            "longitude = ?, " +
            "street_id = ?, " +
            "street_name = ?, " +
            "gender = ?, " +
            "age_range = ?, " +
            "self_defined_ethnicity = ?, " +
            "officer_defined_ethnicity = ?, " +
            "legislation = ?, " +
            "object_of_search = ?, " +
            "outcome = ?, " +
            "outcome_linked_to_object_of_search = ?, " +
            "removal_of_more_than_outer_clothing = ?)";

    @Override
    public void add(List<StopAndSearchesByForce> stopAndSearchesByForce) {
        jdbcTemplate.batchUpdate(SQL_ADD_STOP_AND_SEARCHES_BY_FORCE,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, stopAndSearchesByForce.get(i).getType());
                        ps.setString(2, stopAndSearchesByForce.get(i).getInvolvedPerson());
                        ps.setTimestamp(3, stopAndSearchesByForce.get(i).getDateTime());
                        ps.setString(4, String.valueOf(stopAndSearchesByForce.get(i).getOperation()));
                        ps.setString(5, stopAndSearchesByForce.get(i).getOperationName());
                        ps.setString(6, String.valueOf(stopAndSearchesByForce.get(i).getLatitude()));
                        ps.setString(7, String.valueOf(stopAndSearchesByForce.get(i).getLongitude()));
                        ps.setString(8, String.valueOf(stopAndSearchesByForce.get(i).getStreetId()));
                        ps.setString(9, stopAndSearchesByForce.get(i).getStreetName());
                        ps.setString(10, String.valueOf(stopAndSearchesByForce.get(i).getGender()));
                        ps.setString(11, stopAndSearchesByForce.get(i).getAgeRange());
                        ps.setString(12, stopAndSearchesByForce.get(i).getSelfDefinedEthnicity());
                        ps.setString(13, stopAndSearchesByForce.get(i).getOfficerDefinedEthnicity());
                        ps.setString(14, stopAndSearchesByForce.get(i).getLegislation());
                        ps.setString(15, stopAndSearchesByForce.get(i).getObjectOfSearch());
                        ps.setString(16, stopAndSearchesByForce.get(i).getOutcome());
                        ps.setString(17, String.valueOf(stopAndSearchesByForce.get(i).getOutcomeLinkedToObjectOfSearch()));
                        ps.setString(18, String.valueOf(stopAndSearchesByForce.get(i).getRemovalOfMoreThanOuterClothing()));
                        ps.setString(19, stopAndSearchesByForce.get(i).getType());
                        ps.setString(20, stopAndSearchesByForce.get(i).getInvolvedPerson());
                        ps.setTimestamp(21, stopAndSearchesByForce.get(i).getDateTime());
                        ps.setString(22, String.valueOf(stopAndSearchesByForce.get(i).getOperation()));
                        ps.setString(23, stopAndSearchesByForce.get(i).getOperationName());
                        ps.setString(24, String.valueOf(stopAndSearchesByForce.get(i).getLatitude()));
                        ps.setString(25, String.valueOf(stopAndSearchesByForce.get(i).getLongitude()));
                        ps.setString(26, String.valueOf(stopAndSearchesByForce.get(i).getStreetId()));
                        ps.setString(27, stopAndSearchesByForce.get(i).getStreetName());
                        ps.setString(28, String.valueOf(stopAndSearchesByForce.get(i).getGender()));
                        ps.setString(29, stopAndSearchesByForce.get(i).getAgeRange());
                        ps.setString(30, stopAndSearchesByForce.get(i).getSelfDefinedEthnicity());
                        ps.setString(31, stopAndSearchesByForce.get(i).getOfficerDefinedEthnicity());
                        ps.setString(32, stopAndSearchesByForce.get(i).getLegislation());
                        ps.setString(33, stopAndSearchesByForce.get(i).getObjectOfSearch());
                        ps.setString(34, stopAndSearchesByForce.get(i).getOutcome());
                        ps.setString(35, String.valueOf(stopAndSearchesByForce.get(i).getOutcomeLinkedToObjectOfSearch()));
                        ps.setString(36, String.valueOf(stopAndSearchesByForce.get(i).getRemovalOfMoreThanOuterClothing()));
                    }

                    @Override
                    public int getBatchSize() {
                        return stopAndSearchesByForce.size();
                    }
                });
    }
}
