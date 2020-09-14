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
import java.util.ArrayList;
import java.util.List;

@Repository
public class ApiImpl implements ApiDAO {

    private final static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(ApiImpl.class.getName());

    @Autowired
    private DataSource dataSource;

    @Override
    public List<RequestBody> getDataByRequest(RequestBody requestBody) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        List<RequestBody> finalBody = new ArrayList<>(); // TODO: rename
        try {
            conn = dataSource.getConnection();
            conn.setReadOnly(true);

            preparedStatement = conn.prepareStatement(requestBody.getSql());

            ResultSet resultSet = preparedStatement.executeQuery();
            Integer count = 0;
            while (resultSet.next()) {
                finalBody.add(new RequestBody());
                finalBody.get(count).setReportName(requestBody.getReportName()); // TODO: rework
                finalBody.get(count).setReportDescription(requestBody.getReportDescription());
                finalBody.get(count).setInputs(requestBody.getInputs());
                finalBody.get(count).setOutputs(requestBody.getOutputs());
                finalBody.get(count).setSql(requestBody.getSql());
                for (Integer i = 0; i < requestBody.getOutputs().size(); i++) {
                    finalBody.get(count).setOutputsValue(i, resultSet.getString(requestBody.getOutputsLabel(i)));
                }
                count++;
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

