package pl.com.bottega.photostock.sales.infrastructure.jdbc;

import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.infrastructure.csv.DataAccessException;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.money.Money;
import pl.com.bottega.photostock.sales.model.product.Clip;
import pl.com.bottega.photostock.sales.model.product.Picture;
import pl.com.bottega.photostock.sales.model.product.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by macie on 31.01.2017.
 */
public class JDBCLightBoxRepository implements LightBoxRepository {
    private Integer clientId;
    private static final String GET_CLIENT_ID_SQL = "SELECT id FROM clients WHERE number = ?";
    private static final String GET_LIGHTBOXES_NAMES_SQL = "SELECT Lightboxes.name AS Nazwa " +
            "FROM Lightboxes WHERE Lightboxes.client_id = ?";
    private static final String GET_CLIENT_LIGHTBOXES_SQL = "SELECT Lightboxes.name AS LightboxName, " +
            "Products.id AS ProductNumber, Products.name AS ProductName, Products.priceCents AS ProductPrice, " +
            "Products.priceCurrency AS ProductPriceCurrency, Products.type AS ProductType, Products.length AS Length, " +
            "Products.available AS Available " +
            "FROM Lightboxes INNER JOIN Lightboxes_Products ON Lightboxes.id = Lightboxes_Products.lightbox_id " +
            "INNER JOIN Products ON Lightboxes_Products.product_id = Products.id " +
            "WHERE Lightboxes.client_id = ? ORDER BY Lightboxes.name";
    private static final String GET_CLIENT_LIGHTBOX_SQL = "SELECT Lightboxes.name AS LightboxName, " +
            "Products.id AS ProductNumber, Products.name AS ProductName, Products.priceCents AS ProductPrice, " +
            "Products.priceCurrency AS ProductPriceCurrency, Products.type AS ProductType, Products.length AS Length, " +
            "Products.available AS Available " +
            "FROM Lightboxes INNER JOIN Lightboxes_Products ON Lightboxes.id = Lightboxes_Products.lightbox_id " +
            "INNER JOIN Products ON Lightboxes_Products.product_id = Products.id " +
            "WHERE Lightboxes.client_id = ? AND Lightboxes.name = ?";
    private static final String GET_LIGHTBOX_ID_SQL = "SELECT ID AS LightboxID " +
            "FROM Lightboxes WHERE Lightboxes.client_id = ? AND Lightboxes.name = ?";
    private static final String GET_PRODUCT_ID_FROM_LIGHTBOX_SQL = "SELECT Product_ID AS ProductID " +
            "FROM Lightboxes_Products WHERE Lightbox_ID = ? AND Product_ID = ?";
    private static final String INSERT_NEW_CLIENT_LIGHTBOX_SQL = "INSERT INTO Lightboxes (client_id, name) " +
            "VALUES (?,?)";
    private static final String INSERT_NEW_PRODUCT_TO_CLIENT_LIGHTBOX_SQL = "INSERT INTO Lightboxes_products (lightbox_id, product_id) " +
            "VALUES (?,?)";




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
            throw new DataAccessException(e);
        }
        return lightBoxNames;
    }

    private ResultSet getLightboxesNames(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOXES_NAMES_SQL);
        clientId = getClientId(client);
        preparedStatement.setString(1, String.valueOf(clientId));
        return preparedStatement.executeQuery();
    }

    private Integer getClientId(Client client) throws SQLException {
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
            ResultSet resultSet = getClientLightboxes(client);
            String previousLightBoxName = "";
            String lightBoxName = null;
            Collection<String> tags = null;
            List<Product> items = null;
            Boolean firstLoop = true;
            while (resultSet.next()) {
                lightBoxName = resultSet.getString("LightboxName");
                Product product = null;
                if (!previousLightBoxName.equals(lightBoxName)) {
                    if (!firstLoop)
                        lightBoxes.add(new LightBox(client, previousLightBoxName, items));
                    tags = new ArrayList<>();
                    items = new LinkedList<>();
                    product = checkTypeOfProduct(resultSet, tags, product);
                    items.add(product);
                    previousLightBoxName = lightBoxName;
                } else {
                    product = checkTypeOfProduct(resultSet, tags, product);
                    items.add(product);
                }
                firstLoop = false;

            }
            lightBoxes.add(new LightBox(client, lightBoxName, items));
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return lightBoxes;
    }

    private Product checkTypeOfProduct(ResultSet resultSet, Collection<String> tags, Product product) throws SQLException {
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
        return product;
    }

    private ResultSet getClientLightboxes(Client client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_LIGHTBOXES_SQL);
        Integer clientId = getClientId(client);
        preparedStatement.setString(1, String.valueOf(clientId));
        return preparedStatement.executeQuery();
    }

    @Override
    public LightBox findLightBox(Client client, String lightBoxName) {
        try {
            clientId = getClientId(client);
            ResultSet resultSet = getLightbox(clientId, lightBoxName);
            if (resultSet != null) {
                LightBox lightBox = new LightBox(client, lightBoxName);
                Collection<String> tags = new ArrayList<>();
                Product product = null;
                while (resultSet.next()) {
                    product = checkTypeOfProduct(resultSet, tags, product);
                    lightBox.add(product);
                }
                if (lightBox.getProducts() != null)
                    return lightBox;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return null;
    }

    @Override
    public void put(LightBox lightBox) {
        Client owner = lightBox.getOwner();
        try {
            clientId = getClientId(owner);
            Integer lightboxID = getLightBoxNumber(clientId, lightBox.getName());
            if (lightboxID == null) {
                putNewLightbox(clientId, lightBox.getName());
                lightboxID = getLightBoxNumber(clientId, lightBox.getName());
            }

            Integer productID = null;
            List<Product> items = lightBox.getProducts();
            for (Product product : items) {
                if (getProductIDFromLightbox(lightboxID, Integer.valueOf(product.getNumber())) == null) {
                    putNewProductToLightbox(lightboxID, productID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Integer getProductIDFromLightbox(Integer lightboxID, Integer productID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_ID_FROM_LIGHTBOX_SQL);
        preparedStatement.setString(1, String.valueOf(lightboxID));
        preparedStatement.setString(2, String.valueOf(productID));
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            return null;
        return resultSet.getInt("ProductID");
    }

    private Integer getLightBoxNumber(Integer clientId, String lightBoxName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_LIGHTBOX_ID_SQL);
        preparedStatement.setString(1, String.valueOf(clientId));
        preparedStatement.setString(2, String.valueOf(lightBoxName));
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next())
            return null;
        return resultSet.getInt("LightboxID");
    }


    private void putNewProductToLightbox(Integer lightboxID, Integer productID) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_PRODUCT_TO_CLIENT_LIGHTBOX_SQL);
        preparedStatement.setString(1, String.valueOf(lightboxID));
        preparedStatement.setString(2, String.valueOf(productID));
        preparedStatement.executeUpdate();
    }


    private void putNewLightbox(Integer clientId, String lightBoxName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_CLIENT_LIGHTBOX_SQL);
        preparedStatement.setString(1, String.valueOf(clientId));
        preparedStatement.setString(2, String.valueOf(lightBoxName));
        preparedStatement.executeUpdate();
    }

    private ResultSet getLightbox(Integer clientId, String lightBoxName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_LIGHTBOX_SQL);
        preparedStatement.setString(1, String.valueOf(clientId));
        preparedStatement.setString(2, String.valueOf(lightBoxName));
        return preparedStatement.executeQuery();
    }

}
