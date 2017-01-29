package pl.com.bottega.photostock.sales.presentation.tests;

import pl.com.bottega.photostock.sales.infrastructure.jdbc.JDBCClientRepository;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.ClientRepository;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by macie on 22.01.2017.
 */
public class JDBCClientRepositoryTest {

    public static void main(String[] args) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/photostock", "SA", "");

        ClientRepository clientRepository = new JDBCClientRepository(c);

        Client client = clientRepository.get("100");
        System.out.println(client);
        System.out.println(client.getTransactions().size());
        client.charge(Money.valueOf(20), "bo tak");
        client.recharge(Money.valueOf(40));
        clientRepository.update(client);
    }
}
