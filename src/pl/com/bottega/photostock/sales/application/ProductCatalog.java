package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.module.Client;
import pl.com.bottega.photostock.sales.module.Product;
import pl.com.bottega.photostock.sales.module.ProductRepository;
import pl.com.bottega.photostock.sales.module.money.Money;

import java.util.*;

/**
 * Created by macie on 07.01.2017.
 */
public class ProductCatalog {

    private ProductRepository productRepository;

    public ProductCatalog(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> find(Client client, String nameQuery, String[] tags, Money priceFrom, Money priceTo) {
        //spaekty techniczne (np. dodanie logowania, weryfikacja czy użytkownik ma prawo szukać, transakcje bazodanowe, połączenia z bazą danych
        return productRepository.find(client, nameQuery, tags, priceFrom, priceTo, true);
    }
}
