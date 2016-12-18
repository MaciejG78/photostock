package pl.com.bottega.photostock.sales.module;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by macie on 17.12.2016.
 */
public interface ProductRepository {
    void put(Product product);

    Product get(String number);
}
