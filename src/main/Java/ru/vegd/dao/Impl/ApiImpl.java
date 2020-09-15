package ru.vegd.dao.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.vegd.dao.ApiDAO;
import ru.vegd.httpEntity.RequestBody;
import ru.vegd.httpEntity.ResponseBody;

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
    public List<ResponseBody> getDataByRequest(RequestBody requestBody) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        List<ResponseBody> responseList = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            conn.setReadOnly(true);

            preparedStatement = conn.prepareStatement(requestBody.getSql());

            ResultSet resultSet = preparedStatement.executeQuery();
            Integer count = 0;
            while (resultSet.next()) {
                responseList.add(new ResponseBody());
                for (Integer i = 0; i < requestBody.getOutputs().size(); i++) {
                    responseList.get(count).addResponse();
                    responseList.get(count).setResponseRow(i, resultSet.getString(requestBody.getOutputsLabel(i)));
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
        return responseList;
        }
    }

