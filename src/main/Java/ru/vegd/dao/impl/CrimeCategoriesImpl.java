package ru.vegd.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.CrimeCategoriesDAO;
import ru.vegd.entity.CrimeCategory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CrimeCategoriesImpl implements CrimeCategoriesDAO {

    private final static org.apache.log4j.Logger logger
            = org.apache.log4j.Logger.getLogger(CrimeCategoriesImpl.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_CRIME_CATEGORY = "INSERT INTO crimeCategories(url, name) " +
            "VALUES (?, ?) " +
            "ON CONFLICT (url) DO UPDATE " +
            "SET url = ?, " +
            "name = ?";

    @Override
    public void add(List<CrimeCategory> categoryList) {
        jdbcTemplate.batchUpdate(
                SQL_ADD_CRIME_CATEGORY,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, categoryList.get(i).getUrl());
                        ps.setString(2, categoryList.get(i).getName());
                        ps.setString(3, categoryList.get(i).getUrl());
                        ps.setString(4, categoryList.get(i).getName());
                    }

                    @Override
                    public int getBatchSize() {
                        return categoryList.size();
                    }
                }
        );
    }
}
