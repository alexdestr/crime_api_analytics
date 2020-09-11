package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.ApiDAO;
import ru.vegd.entity.RequestBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ApiImpl implements ApiDAO {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(ApiImpl.class.getName());

    @Autowired
    private DataSource dataSource;

    @Override
    public RequestBody getDataByRequest(RequestBody requestBody) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        RequestBody finalBody = null;
        try {
            conn = dataSource.getConnection();
            conn.setReadOnly(true);

            preparedStatement = conn.prepareStatement(requestBody.getSql());

            finalBody = new RequestBody();
            finalBody.setReportName(requestBody.getReportName());
            finalBody.setReportDescription(requestBody.getReportDescription());
            finalBody.setInputs(requestBody.getInputs());
            finalBody.setOutputs(requestBody.getOutputs());
            finalBody.setSql(requestBody.getSql());

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                for (Integer i = 0; i < requestBody.getOutputs().size(); i++) {
                    finalBody.setOutputsValue(i, resultSet.getString(requestBody.getOutputsLabel(i)));
                }
            }

        } catch (SQLException e) {
            logger.warn("SQL Exception: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.warn("Can't close preparedStatement. " + e.getMessage());
                }
            }
            try {
                if (!(conn != null && conn.isClosed())) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.warn("Can't return connection. " + e.getMessage());
            }
        }
        return finalBody;
        }
    }

