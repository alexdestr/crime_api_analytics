package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.ForcesListDao;
import ru.vegd.entity.ForcesList;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ForcesListImpl implements ForcesListDao {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ForcesListImpl.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_FORCE = "INSERT INTO forcesList " +
            "VALUES (?, ?) " +
            "ON CONFLICT(id) DO UPDATE " +
            "SET id=?, " +
            "name=?";

    @Override
    public void add(List<ForcesList> forcesList) {
        jdbcTemplate.batchUpdate(SQL_ADD_FORCE, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, forcesList.get(i).getId());
                ps.setString(2, forcesList.get(i).getName());
                ps.setString(3, forcesList.get(i).getId());
                ps.setString(4, forcesList.get(i).getName());
            }

            @Override
            public int getBatchSize() {
                return forcesList.size();
            }
        });
    }
}
