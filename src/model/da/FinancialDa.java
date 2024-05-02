package model.da;

import model.entity.FType;
import model.entity.Financial;
import model.tools.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinancialDa implements AutoCloseable {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public FinancialDa() throws SQLException {
        connection = JDBC.getJdbc().getConnection();
    }

    public void save(Financial financial) throws Exception {
        preparedStatement = connection.prepareStatement(
                "INSERT INTO FINANCIAL (ID, AMOUNT, F_TYPE, DATE_TIME, DESCRIPTION) VALUES (FINANCIAL_SEQ.nextval,?,?,?,?)"
        );
        preparedStatement.setInt(1, financial.getAmount());
        preparedStatement.setString(2, financial.getFType().name());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(financial.getDateTime()));
        preparedStatement.setString(4, financial.getDescription());
        preparedStatement.execute();
    }

    public void edit(Financial financial) throws Exception {
        preparedStatement = connection.prepareStatement(
                "UPDATE FINANCIAL SET AMOUNT=?, F_TYPE=?, DATE_TIME=?, DESCRIPTION=? WHERE ID=?"
        );
        preparedStatement.setInt(1, financial.getAmount());
        preparedStatement.setString(2, financial.getFType().name());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(financial.getDateTime()));
        preparedStatement.setString(4, financial.getDescription());
        preparedStatement.setInt(5, financial.getId());
        preparedStatement.execute();
    }

    public void remove(int id) throws Exception {
        preparedStatement = connection.prepareStatement(
                "DELETE FROM FINANCIAL WHERE ID=?"
        );
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    public List<Financial> findAll() throws Exception{
        preparedStatement = connection.prepareStatement("SELECT * FROM FINANCIAL ORDER BY ID, F_TYPE, DATE_TIME");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Financial> financialList = new ArrayList<>();

        while (resultSet.next()) {
            Financial financial = Financial
                    .builder()
                    .id(resultSet.getInt("ID"))
                    .amount(resultSet.getInt("AMOUNT"))
                    .fType(FType.valueOf(resultSet.getString("F_TYPE")))
                    .dateTime(resultSet.getTimestamp("DATE_TIME").toLocalDateTime())
                    .description((resultSet.getString("DESCRIPTION")))
                    .build();
            financialList.add(financial);
        }
        return financialList;
    }

    public Financial findById(int id) throws Exception{
        preparedStatement = connection.prepareStatement("SELECT * FROM FINANCIAL WHERE ID=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Financial financial = null;
        if (resultSet.next()) {
            financial = Financial
                    .builder()
                    .id(resultSet.getInt("ID"))
                    .amount(resultSet.getInt("AMOUNT"))
                    .fType(FType.valueOf(resultSet.getString("F_TYPE")))
                    .dateTime(resultSet.getTimestamp("DATE_TIME").toLocalDateTime())
                    .description((resultSet.getString("DESCRIPTION")))
                    .build();
        }
        return financial;
    }

    public List<Financial> findByFType(String fType) throws Exception{
        preparedStatement = connection.prepareStatement("SELECT * FROM FINANCIAL WHERE F_TYPE LIKE ?");
        preparedStatement.setString(1, fType+"%");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<Financial> financialList = new ArrayList<>();

        while (resultSet.next()) {
            Financial financial = Financial
                    .builder()
                    .id(resultSet.getInt("ID"))
                    .amount(resultSet.getInt("AMOUNT"))
                    .fType(FType.valueOf(resultSet.getString("F_TYPE")))
                    .dateTime(resultSet.getTimestamp("DATE_TIME").toLocalDateTime())
                    .description((resultSet.getString("DESCRIPTION")))
                    .build();
            financialList.add(financial);
        }
        return financialList;
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}
