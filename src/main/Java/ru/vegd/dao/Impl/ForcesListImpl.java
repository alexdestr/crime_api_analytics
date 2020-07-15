package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.ForcesListDAO;
import ru.vegd.entity.Force;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ForcesListImpl implements ForcesListDAO {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ForcesListImpl.class.getName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SQL_ADD_FORCE = "INSERT INTO forcesList " +
            "VALUES (?, ?) " +
            "ON CONFLICT(id) DO UPDATE " +
            "SET id=?, " +
            "name=?";
    private static final String SQL_GET_ALL_FORCES = "SELECT id, name " +
            "FROM forcesList";

    @Override
    public void add(List<Force> force) {
        jdbcTemplate.batchUpdate(SQL_ADD_FORCE, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, force.get(i).getId());
                ps.setString(2, force.get(i).getName());
                ps.setString(3, force.get(i).getId());
                ps.setString(4, force.get(i).getName());
            }

            @Override
            public int getBatchSize() {
                return force.size();
            }
        });
    }

    @Override
    public List<Force> getAllForces() {
        return jdbcTemplate.query(SQL_GET_ALL_FORCES,
                (rs, rowNum) -> new Force(
                        rs.getString("id"),
                        rs.getString("name")
                ));
    }
}
