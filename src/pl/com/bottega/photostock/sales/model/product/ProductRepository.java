package pl.com.bottega.photostock.sales.model.product;

import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.List;

/**
 * Created by macie on 17.12.2016.
 */
public interface ProductRepository {
    void put(Product product);

    Product get(String number);

    List<Product> find(Client client, String nameQuery, String[] tags, Money priceFrom, Money priceTo, boolean onlyActive);

    Product checkIfAvailable(String product, Boolean onlyAvailable);

}
