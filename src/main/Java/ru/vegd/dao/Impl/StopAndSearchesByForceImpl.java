package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StopAndSearchesByForceDAO;
import ru.vegd.entity.StopAndSearchesByForce;
import ru.vegd.utils.SQLParser;

import java.sql.*;
import java.time.YearMonth;
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
            "ON CONFLICT(type, datetime, age_range, gender, officer_defined_ethnicity, legislation, self_defined_ethnicity) " +
            "DO UPDATE " +
            "SET " +
            "type = ?, " +
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
            "removal_of_more_than_outer_clothing = ?";

    private static final String PATH_TO_SQL_QUERY_STOP_AND_SEARCHES_STATISTIC_BY_ETHNICITY = "db/scripts/script#4/stopAndSearchesStatisticByEthnicity.sql";

    @Override
    public void add(List<StopAndSearchesByForce> stopAndSearchesByForce) {
        jdbcTemplate.batchUpdate(SQL_ADD_STOP_AND_SEARCHES_BY_FORCE,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, stopAndSearchesByForce.get(i).getType());
                        ps.setString(2, stopAndSearchesByForce.get(i).getInvolvedPerson());
                        ps.setTimestamp(3, stopAndSearchesByForce.get(i).getDateTime());
                        if (stopAndSearchesByForce.get(i).getOperation() != null) {
                            ps.setBoolean(4, stopAndSearchesByForce.get(i).getOperation());
                        } else {
                            ps.setNull(4, Types.BOOLEAN);
                        }
                        ps.setString(5, stopAndSearchesByForce.get(i).getOperationName());
                        if (stopAndSearchesByForce.get(i).getLatitude() != null && stopAndSearchesByForce.get(i).getLongitude() != null) {
                            ps.setDouble(6, stopAndSearchesByForce.get(i).getLatitude());
                            ps.setDouble(7, stopAndSearchesByForce.get(i).getLongitude());
                        } else {
                            ps.setNull(6, Types.DECIMAL);
                            ps.setNull(7, Types.DECIMAL);
                        }
                        if (stopAndSearchesByForce.get(i).getStreetId() != null && stopAndSearchesByForce.get(i).getStreetName() != null) {
                            ps.setDouble(8, stopAndSearchesByForce.get(i).getStreetId());
                            ps.setString(9, stopAndSearchesByForce.get(i).getStreetName());
                        } else {
                            ps.setNull(8, Types.DECIMAL);
                            ps.setNull(9, Types.OTHER);
                        }
                        ps.setString(10, String.valueOf(stopAndSearchesByForce.get(i).getGender()));
                        ps.setString(11, stopAndSearchesByForce.get(i).getAgeRange());
                        ps.setString(12, stopAndSearchesByForce.get(i).getSelfDefinedEthnicity());
                        ps.setString(13, stopAndSearchesByForce.get(i).getOfficerDefinedEthnicity());
                        ps.setString(14, stopAndSearchesByForce.get(i).getLegislation());
                        ps.setString(15, stopAndSearchesByForce.get(i).getObjectOfSearch());
                        ps.setString(16, stopAndSearchesByForce.get(i).getOutcome());
                        if (stopAndSearchesByForce.get(i).getOutcomeLinkedToObjectOfSearch() != null) {
                            ps.setBoolean(17, stopAndSearchesByForce.get(i).getOutcomeLinkedToObjectOfSearch());
                        } else {
                            ps.setNull(17, Types.BOOLEAN);
                        }
                        if (stopAndSearchesByForce.get(i).getRemovalOfMoreThanOuterClothing() != null) {
                            ps.setBoolean(18, stopAndSearchesByForce.get(i).getRemovalOfMoreThanOuterClothing());
                        } else {
                            ps.setNull(18, Types.BOOLEAN);
                        }
                        ps.setString(19, stopAndSearchesByForce.get(i).getType());
                        ps.setString(20, stopAndSearchesByForce.get(i).getInvolvedPerson());
                        ps.setTimestamp(21, stopAndSearchesByForce.get(i).getDateTime());
                        if (stopAndSearchesByForce.get(i).getOperation() != null) {
                            ps.setBoolean(22, stopAndSearchesByForce.get(i).getOperation());
                        } else {
                            ps.setNull(22, Types.BOOLEAN);
                        }
                        ps.setString(23, stopAndSearchesByForce.get(i).getOperationName());
                        if (stopAndSearchesByForce.get(i).getLatitude() != null && stopAndSearchesByForce.get(i).getLongitude() != null) {
                            ps.setDouble(24, stopAndSearchesByForce.get(i).getLatitude());
                            ps.setDouble(25, stopAndSearchesByForce.get(i).getLongitude());
                        } else {
                            ps.setNull(24, Types.DECIMAL);
                            ps.setNull(25, Types.DECIMAL);
                        }
                        if (stopAndSearchesByForce.get(i).getStreetId() != null && stopAndSearchesByForce.get(i).getStreetName() != null) {
                            ps.setDouble(26, stopAndSearchesByForce.get(i).getStreetId());
                            ps.setString(27, stopAndSearchesByForce.get(i).getStreetName());
                        } else {
                            ps.setNull(26, Types.DECIMAL);
                            ps.setNull(27, Types.OTHER);
                        }
                        ps.setString(28, String.valueOf(stopAndSearchesByForce.get(i).getGender()));
                        ps.setString(29, stopAndSearchesByForce.get(i).getAgeRange());
                        ps.setString(30, stopAndSearchesByForce.get(i).getSelfDefinedEthnicity());
                        ps.setString(31, stopAndSearchesByForce.get(i).getOfficerDefinedEthnicity());
                        ps.setString(32, stopAndSearchesByForce.get(i).getLegislation());
                        ps.setString(33, stopAndSearchesByForce.get(i).getObjectOfSearch());
                        ps.setString(34, stopAndSearchesByForce.get(i).getOutcome());
                        if (stopAndSearchesByForce.get(i).getOutcomeLinkedToObjectOfSearch() != null) {
                            ps.setBoolean(35, stopAndSearchesByForce.get(i).getOutcomeLinkedToObjectOfSearch());
                        } else {
                            ps.setNull(35, Types.BOOLEAN);
                        }
                        if (stopAndSearchesByForce.get(i).getRemovalOfMoreThanOuterClothing() != null) {
                            ps.setBoolean(36, stopAndSearchesByForce.get(i).getRemovalOfMoreThanOuterClothing());
                        } else {
                            ps.setNull(36, Types.BOOLEAN);
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return stopAndSearchesByForce.size();
                    }
                });
    }

    @Override
    public void getStatisticByEthnicity(YearMonth from, YearMonth to) {
        String sqlScript = SQLParser.parseSQLFileToString(PATH_TO_SQL_QUERY_STOP_AND_SEARCHES_STATISTIC_BY_ETHNICITY);
        jdbcTemplate.query(sqlScript, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, String.valueOf(from));
                ps.setString(2, String.valueOf(to));
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println("--------------");
                System.out.println("Officer Defined Ethnicity: " + rs.getString("officer_defined_ethnicity"));
                System.out.println("Stop And Search Count: " + rs.getString("count"));
                System.out.println("Arrest Rate: " + rs.getString("arrest_rate"));
                System.out.println("Release Rate: " + rs.getString("release_count"));
                System.out.println("Other Outcomes Rate: " + rs.getString("other_outcomes_count"));
                System.out.println("Most Popular Object Of Search: " + rs.getString("object_of_search"));
            }
        });
    }

}
