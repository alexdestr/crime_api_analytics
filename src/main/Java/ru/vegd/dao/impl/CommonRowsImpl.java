package ru.vegd.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.CommonRowsDAO;
import ru.vegd.utils.SQLParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommonRowsImpl implements CommonRowsDAO {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CommonRowsImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String PATH_TO_SQL_QUERY_COMPARSION_STOP_AND_SEARCHES_WITH_STREET_LEVEL_CRIMES = "db/scripts/script#6/stop_and_search_correlation_with_crimes.sql";

    @Override
    public List<String> comparsionStopAndSearchesWithStreetLevelCrimes(YearMonth from, YearMonth to) {
        String sqlScript = SQLParser.parseSQLFileToString(PATH_TO_SQL_QUERY_COMPARSION_STOP_AND_SEARCHES_WITH_STREET_LEVEL_CRIMES);
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
                        output.append("Street Id: ").append(rs.getString("street_id")).append("\n");
                        output.append("Street Name: ").append(rs.getString("street_name")).append("\n");
                        output.append("Month: ").append(rs.getString("month")).append("\n");
                        output.append("Drugs Crime Count: ").append(rs.getString("drugs_crime_count")).append("\n");
                        output.append("Drugs Strop And Search Count: ").append(rs.getString("drugs_stop_and_search_count")).append("\n");
                        output.append("Weapons Crimes Count: ").append(rs.getString("weapons_crimes_count")).append("\n");
                        output.append("Weapons Stop And Search Count: ").append(rs.getString("weapons_stop_and_search_count")).append("\n");
                        output.append("Theft Crimes Count: ").append(rs.getString("theft_crimes_count")).append("\n");
                        output.append("Theft Stop And Search Count: ").append(rs.getString("theft_stop_and_search_count"));
                        finalOutput.add(String.valueOf(output));
                    }
                });
        return finalOutput;
    }
}