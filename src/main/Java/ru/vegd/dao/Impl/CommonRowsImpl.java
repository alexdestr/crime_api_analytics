package ru.vegd.dao.Impl;

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

@Repository
public class CommonRowsImpl implements CommonRowsDAO {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CommonRowsImpl.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String PATH_TO_SQL_QUERY_COMPARSION_STOP_AND_SEARCHES_WITH_STREET_LEVEL_CRIMES = "db/scripts/script#6/stopAndSearchCorrelationWithCrimes.sql";

    @Override
    public void comparsionStopAndSearchesWithStreetLevelCrimes(YearMonth from, YearMonth to) {
        String sqlScript = SQLParser.parseSQLFileToString(PATH_TO_SQL_QUERY_COMPARSION_STOP_AND_SEARCHES_WITH_STREET_LEVEL_CRIMES);
        jdbcTemplate.query(sqlScript, new PreparedStatementSetter() {
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
                System.out.println("--------------");
                System.out.println("Street Id: " + rs.getString("street_id"));
                System.out.println("Street Name: " + rs.getString("street_name"));
                System.out.println("Month: " + rs.getString("month"));
                System.out.println("Drugs Crime Count: " + rs.getString("drugs_crime_count"));
                System.out.println("Drugs Strop And Search Count: " + rs.getString("drugs_stop_and_search_count"));
                System.out.println("Weapons Crimes Count: " + rs.getString("weapons_crimes_count"));
                System.out.println("Weapons Stop And Search Count: " + rs.getString("weapons_stop_and_search_count"));
                System.out.println("Theft Crimes Count: " + rs.getString("theft_crimes_count"));
                System.out.println("Theft Stop And Search Count: " + rs.getString("theft_stop_and_search_count"));
            }
        });
    }
}
