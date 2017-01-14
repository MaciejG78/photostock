package pl.com.bottega.photostock.sales.model.product;

/**
 * Created by macie on 18.12.2016.
 */
public class ProductNotAvailableException extends RuntimeException {

    public ProductNotAvailableException(Product product) {
        super(String.format("Product %s is not an available", product.getNumber()));


    }


}
