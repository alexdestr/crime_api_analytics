package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.ApiDAO;
import ru.vegd.entity.RequestBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApiImpl implements ApiDAO {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(ApiImpl.class.getName());

    @Autowired
    private DataSource dataSource;

    @Override
    public List getDataByRequest(RequestBody requestBody) {
        try {
            Connection conn = dataSource.getConnection();
            conn.setReadOnly(true);
            PreparedStatement preparedStatement = conn.prepareStatement(requestBody.getSql());

            

        } catch (SQLException e) {
            logger.warn("SQL Exception: " + e.getMessage());
        }
        return null;
    }
}
