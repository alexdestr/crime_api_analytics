package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.StreetLevelCrimesDAO;
import ru.vegd.entity.StreetLevelCrime;
import ru.vegd.utils.SQLParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
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

    private static final String PATH_TO_SQL_QUERY_MOST_DANGEROUS_STREET = "db/scripts/script#1/mostDangerousStreets.sql";
    private static final String PATH_TO_SQL_QUERY_CRIME_VOLUME_COMPARSION = "db/scripts/script#2/crimeVolumeComparsion.sql";
    private static final String PATH_TO_SQL_QUERY_CRIMES_WITH_SPECIFIED_OUTCOME_STATUS = "db/scripts/script#3/crimesWithSpecifiedOutcomeStatus.sql";

    @Override
    public void add(List<StreetLevelCrime> crimeList) {
        jdbcTemplate.batchUpdate(
                SQL_ADD_CRIME,
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

    @Override
    public List<String> getMostDangerousStreets(YearMonth from, YearMonth to) {
        String sqlScript = SQLParser.parseSQLFileToString(PATH_TO_SQL_QUERY_MOST_DANGEROUS_STREET);
        List<String> finalOutput = new ArrayList<>();
        jdbcTemplate.query(
                sqlScript,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, String.valueOf(from));
                        ps.setString(2, String.valueOf(to));
                        ps.setString(3, String.valueOf(from));
                        ps.setString(4, String.valueOf(to));
                    }
                }, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        StringBuilder output;
                        output = new StringBuilder("--------------");
                        output.append("\n");
                        output.append("Street ID: ").append(rs.getString("street_id")).append("\n");
                        output.append("Street Name: ").append(rs.getString("street_name")).append("\n");
                        output.append("Period: ").append(rs.getString("period")).append("\n");
                        output.append("Crimes Count: ").append(rs.getString("cnt"));
                        finalOutput.add(String.valueOf(output));
                    }
                });
        return finalOutput;
    }

    @Override
    public List<String> getMonthToMonthCrimeVolumeComparison(YearMonth from, YearMonth to) {
        String sqlScript = SQLParser.parseSQLFileToString(PATH_TO_SQL_QUERY_CRIME_VOLUME_COMPARSION);
        List<String> finalOutput = new ArrayList<>();
        jdbcTemplate.query(
                sqlScript,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, String.valueOf(from));
                        ps.setString(2, String.valueOf(to));

                    }
                }, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        StringBuilder output;
                        output = new StringBuilder("--------------");
                        output.append("\n");
                        output.append("Category: ").append(rs.getString("category")).append("\n");
                        output.append("Month: ").append(rs.getString("mth")).append("\n");
                        output.append("Previous Month Count: ").append(rs.getString("previous_month_count")).append("\n");
                        output.append("Current Month Count: ").append(rs.getString("current_month_count")).append("\n");
                        output.append("Delta: ").append(rs.getString("delta")).append("\n");
                        output.append("Growth Rate: ").append(rs.getString("growth_rate"));
                        finalOutput.add(String.valueOf(output));
                    }
                });
        return finalOutput;
    }

    @Override
    public List<String> getCrimesWithSpecifiedOutcomeStatus(String outcomeCategory, YearMonth from, YearMonth to) {
        String sqlScript = SQLParser.parseSQLFileToString(PATH_TO_SQL_QUERY_CRIMES_WITH_SPECIFIED_OUTCOME_STATUS);
        List<String> finalOutput = new ArrayList<>();
        jdbcTemplate.query(
                sqlScript,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, outcomeCategory);
                        ps.setString(2, String.valueOf(from));
                        ps.setString(3, String.valueOf(to));
                        ps.setString(4, String.valueOf(from));
                        ps.setString(5, String.valueOf(to));
                    }
                }, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        StringBuilder output;
                        output = new StringBuilder("--------------");
                        output.append("\n");
                        output.append("Street Id: ").append(rs.getString("street_id")).append("\n");
                        output.append("Street Name: ").append(rs.getString("street_name")).append("\n");
                        output.append("Outcome Category: ").append(rs.getString("outcome_category")).append("\n");
                        output.append("Percentage Of The Total Crimes: ").append(rs.getString("percentage_of_the_total_crimes"));
                        finalOutput.add(String.valueOf(output));
                    }
                });
        return finalOutput;
    }
}
