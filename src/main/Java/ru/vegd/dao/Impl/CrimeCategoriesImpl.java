package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.CrimeCategoriesDao;
import ru.vegd.entity.CrimeCategories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class CrimeCategoriesImpl implements CrimeCategoriesDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_CRIME_CATEGORY = "INSERT INTO crimeCategories(url, name) " +
            "VALUES (?, ?) " +
            "ON CONFLICT (url) DO UPDATE " +
            "SET url = ?, " +
            "name = ?";

    @Override
    public void addCrimeCategory(CrimeCategories crimeCategories) {
        jdbcTemplate.batchUpdate(SQL_ADD_CRIME_CATEGORY,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, crimeCategories.getUrl());
                        ps.setString(2, crimeCategories.getName());
                        ps.setString(3, crimeCategories.getUrl());
                        ps.setString(4, crimeCategories.getName());
                    }

                    @Override
                    public int getBatchSize() {
                        return 1;
                    }
                }

        );
    }
}
