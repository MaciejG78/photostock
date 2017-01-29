package pl.com.bottega.photostock.sales.infrastructure.jdbc;

import pl.com.bottega.photostock.sales.infrastructure.csv.DataAccessException;
import pl.com.bottega.photostock.sales.model.client.*;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.money.Money;
import pl.com.bottega.photostock.sales.model.product.Clip;
import pl.com.bottega.photostock.sales.model.product.Picture;
import pl.com.bottega.photostock.sales.model.product.Product;

import java.sql.*;
import java.util.*;

/**
 * Created by macie on 22.01.2017.
 */
public class JDBCLightBoxRepository implements LightBoxRepository {

    private static final String GET_CLIENT_ID_SQL = "SELECT id FROM clients WHERE number = ?";
    private static final String GET_LIGHTBOXES_NAMES_SQL = "SELECT Lightboxes.name AS Nazwa " +
            "FROM Lightboxes WHERE Lightboxes.client_id = ?";
    private static final String GET_PRODUCTS_IN_LIGHTBOX = "SELECT Products.id AS ProductNumber, Products.name AS ProductName, " +
            "Products.priceCents AS ProductPrice, Products.priceCurrency AS ProductPriceCurrency, Products.type AS ProductType, " +
            "Products.length AS Length, Products.available AS Available " +
            "FROM Lightboxes " +
            "INNER JOIN Lightboxes_Products ON Lightboxes.id = Lightboxes_Products.lightbox_id " +
            "INNER JOIN Products ON Lightboxes_Products.product_id = Products.id " +
            "WHERE Lightboxes.client_id = ? AND Lightboxes.name = ?";
    private Connection connection;

    public JDBCLightBoxRepository(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Collection<String> getLightBoxNames(Client client) {
        Collection<String> lightBoxNames = new LinkedList<>();
        try {
            ResultSet resultSet = getLightboxesNames(client);
            while (resultSet.next()) {
                lightBoxNames.add(parseLightBoxName(resultSet));
            }
        } catch (SQLException e) {
            new DataAccessException(e);
        }
        return lightBoxNames;
    }

    private ResultSet getLightboxesNames(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOXES_NAMES_SQL);
        Integer clientId = getClientId(client);
        preparedStatement.setString(1, String.valueOf(clientId));
        return preparedStatement.executeQuery();
    }

    private int getClientId(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_ID_SQL);
        preparedStatement.setString(1, client.getNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getInt("id");
    }

    private String parseLightBoxName(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("Nazwa");
        return name;
    }

    @Override
    public Collection<LightBox> getFor(Client client) {
        Collection<LightBox> lightBoxes = new HashSet<>();
        try {
            ResultSet resultSet = getLightboxesNames(client);

            while (resultSet.next()) {
                String lightBoxName = parseLightBoxName(resultSet);
                lightBoxes.add(new LightBox(client, lightBoxName, getProducts(getClientId(client), lightBoxName)));
            }
        } catch (SQLException e) {
            new DataAccessException(e);
        }
        return lightBoxes;
    }

    private List<Product> getProducts(Integer clientId, String lightBoxName) {
        List<Product> items = new LinkedList<>();
        Collection<String> tags = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCTS_IN_LIGHTBOX);
            preparedStatement.setString(1, String.valueOf(clientId));
            preparedStatement.setString(2, lightBoxName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = null;
                if (resultSet.getString("ProductType").equals("Picture")) {
                    product = new Picture(resultSet.getString("ProductNumber"),
                            resultSet.getString("ProductName"),
                            tags, Money.valueOf(resultSet.getInt("ProductPrice")),
                            resultSet.getBoolean("Available"));
                } else {
                    if (resultSet.getString("ProductType").equals("Clip")) {
                        product = new Clip(resultSet.getString("ProductNumber"),
                                resultSet.getString("ProductName"),
                                resultSet.getLong("Length"),
                                Money.valueOf(resultSet.getInt("ProductPrice")),
                                resultSet.getBoolean("Available"));
                    }
                }
                items.add(product);
            }
        } catch (SQLException e) {
            new DataAccessException(e);
        }
        return items;
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) {
        Collection<LightBox> lightBoxes = getFor(client);
        if (lightBoxes != null)
            for (LightBox lb : lightBoxes)
                if (lb.getName().equals(lightBoxName))
                    return lb;
        return null;
    }

    @Override
    public void put(LightBox lightBox) {

    }


}