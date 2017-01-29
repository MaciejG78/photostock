package pl.com.bottega.photostock.sales.infrastructure.jdbc;

import pl.com.bottega.photostock.sales.infrastructure.csv.DataAccessException;
import pl.com.bottega.photostock.sales.model.client.*;
import pl.com.bottega.photostock.sales.model.money.IntegerMoney;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by macie on 22.01.2017.
 */
public class JDBCClientRepository implements ClientRepository {

    private static final String GET_CLIENT_SQL = "SELECT * FROM clients WHERE number = ?";
    private static final String GET_TRANSACTIONS_SQL = "SELECT * FROM transactions WHERE client_id = ?";
    private static final String UPDATE_CLIENT_SQL = "UPDATE clients SET name=?, active=?, status=?, balance=?, creditLimit=? WHERE number=?";
    private static final String GET_CLIENT_ID_SQL = "SELECT id FROM clients WHERE number = ?";
    private static final String GET_TRANSACTIONS_COUNT_SQL = "SELECT count(*) AS total FROM transactions WHERE client_id = ?";
    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO transactions (client_id, value, description, date) VALUES (?, ?, ?, ?)";
    private Connection connection;

    public JDBCClientRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client get(String clientNumber) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_SQL);
            preparedStatement.setString(1, clientNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            return parseClient(resultSet);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private Client parseClient(ResultSet resultSet) throws SQLException {
        String number = resultSet.getString("number");
        String name = resultSet.getString("name");
        boolean active = resultSet.getBoolean("active");
        ClientStatus status = ClientStatus.valueOf(resultSet.getString("status").trim());
        Money balance = new IntegerMoney(resultSet.getLong("balance"), Money.DEFAULT_CURRENCY);
        Integer clientId = resultSet.getInt("id");
        if (status == ClientStatus.VIP) {
            Money creditLimit = new IntegerMoney(resultSet.getLong("creditLimit"), Money.DEFAULT_CURRENCY);
            return new VipClient(number, name, new Address(), balance, creditLimit, active, getTransactions(clientId));
        }
        return new Client(number, name, new Address(), status, balance, active, getTransactions(clientId));

    }

    private Collection<Transaction> getTransactions(Integer clientId) throws SQLException {
        Collection<Transaction> transactions = new LinkedList<>();
        PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_SQL);
        preparedStatement.setInt(1, clientId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Transaction transaction = parseTransaction(resultSet);
            transactions.add(transaction);
        }
        return transactions;
    }

    private Transaction parseTransaction(ResultSet resultSet) throws SQLException {
        Money value = new IntegerMoney(resultSet.getLong("value"), Money.DEFAULT_CURRENCY);
        String description = resultSet.getString("description");
        LocalDateTime localDateTime = resultSet.getTimestamp("date").toLocalDateTime();
        return new Transaction(value, description, localDateTime);
    }

    @Override
    //name=?, active=?, status=?, balance=?, creditLimit=?
    public void update(Client client) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT_SQL);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setBoolean(2, client.isActive());
            preparedStatement.setString(3, client.getStatus().toString());
            preparedStatement.setLong(4, client.getBalance().convertToInteger().toCents());
            if (client instanceof VipClient) {
                VipClient vipClient = (VipClient) client;
                preparedStatement.setLong(5, vipClient.getCreditLimit().convertToInteger().toCents());
            } else
                preparedStatement.setLong(5, 0);
            preparedStatement.setString(6, client.getNumber());
            preparedStatement.executeUpdate();
            updateTransactions(client);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

    private void updateTransactions(Client client) throws SQLException {
        int clientId = getClientId(client);
        int transactionsCount = getTransactionsCount(clientId);
        if (client.getTransactions().size() == transactionsCount)
            return;
        List<Transaction> transactions = new ArrayList<>(client.getTransactions());
        transactions.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
        transactions = transactions.subList(transactionsCount, transactions.size());
        for (Transaction transaction : transactions)
            insertTransaction(clientId, transaction);
    }

    private void insertTransaction(int clientId, Transaction transaction) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_SQL);
        preparedStatement.setInt(1, clientId);
        preparedStatement.setLong(2, transaction.getValue().convertToInteger().toCents());
        preparedStatement.setString(3, transaction.getDescription());
        preparedStatement.setTimestamp(4, Timestamp.valueOf(transaction.getTimestamp()));
        preparedStatement.executeUpdate();
    }

    private int getTransactionsCount(int clientId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_COUNT_SQL);
        preparedStatement.setInt(1, clientId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("total");
    }

    private int getClientId(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_ID_SQL);
        preparedStatement.setString(1, client.getNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }
}