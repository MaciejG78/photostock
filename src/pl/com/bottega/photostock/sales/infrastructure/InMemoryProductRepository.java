package pl.com.bottega.photostock.sales.infrastructure;

import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.money.Money;
import pl.com.bottega.photostock.sales.model.product.Clip;
import pl.com.bottega.photostock.sales.model.product.Picture;
import pl.com.bottega.photostock.sales.model.product.Product;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;

import java.util.*;

/**
 * Created by macie on 17.12.2016.
 */
public class InMemoryProductRepository implements ProductRepository {

    private static final Map<String, Product> REPOSITORY = new HashMap<>(); //Jakby było bez static to obrazki przechowywały by się na czas życia repozytorium
    //sens użycia final jest taki, że można dodawac do Mapy ale nie można zmienić referencji na co wskazuje memoryPictureRepository

    static {
        Collection<String> tags = Arrays.asList("przyroda", "motoryzacja");
        Product product1 = new Picture("1", "BMW i335 M", tags, Money.valueOf(4));
        Product product2 = new Picture("2", "Mercedes sls", tags, Money.valueOf(3));
        Product picture3 = new Picture("3", "Porsche 911", tags, Money.valueOf(5));
        Product product4 = new Picture("4", "Ford Focus ST", tags, Money.valueOf(2));
        Product product5 = new Picture("5", "Fiat 500i", tags, Money.valueOf(0));
        Product product6 = new Picture("6", "BMW i8", tags, Money.valueOf(6));

        Product clip1 = new Clip("7", "Wściekłe pięści węża", 2L * 1000 * 60 * 2, Money.valueOf(5));
        Product clip2 = new Clip("8", "Sum tzw. olimpijczyk", 40L * 1000 * 60 * 2, Money.valueOf(10));

        REPOSITORY.put("1", product1);
        REPOSITORY.put("2", product2);
        REPOSITORY.put("3", picture3);
        REPOSITORY.put("4", product4);
        REPOSITORY.put("5", product5);
        REPOSITORY.put("6", product6);
        REPOSITORY.put("7", clip1);
        REPOSITORY.put("8", clip2);

    }

    @Override
    public void put(Product product) {
        REPOSITORY.put(product.getNumber(), product);
    }

    @Override
    public Product get(String number) {
        return REPOSITORY.get(number);
    }

    @Override
    public List<Product> find(Client client, String nameQuery, String[] tags, Money priceFrom, Money priceTo, boolean onlyActive) {
        List<Product> matchingProducts = new LinkedList<>();
        for (Product product : REPOSITORY.values()) {
            if (matches(client, product, nameQuery, tags, priceFrom, priceTo, onlyActive))
                matchingProducts.add(product);
        }
        return matchingProducts;
    }

    private boolean matches(Client client, Product product, String nameQuery, String[] tags, Money priceFrom, Money priceTo, boolean onlyActive) {

        return matchesQuery(product, nameQuery) &&
                matchesTags(product, tags) &&
                matchesPriceFrom(client, product, priceFrom) &&
                matchesPriceTo(client, product, priceTo);
    }

    private boolean matchesPriceTo(Client client, Product product, Money priceTo) {
        return priceTo == null || product.calculatePrice(client).lte(priceTo);
    }

    private boolean matchesPriceFrom(Client client, Product product, Money priceFrom) {
        return priceFrom == null || product.calculatePrice(client).gte(priceFrom);
    }

    private boolean matchesTags(Product product, String[] tags) {
        if (tags == null || tags.length == 0)
            return true;
        if (!(product instanceof Picture))
            return false;
        Picture picture = (Picture) product;
        for (String tag : tags) {
            if (!picture.hasTag(tag))
                return false;
        }
        return true;
    }

    private boolean matchesQuery(Product product, String nameQuery) {
        return (nameQuery == null || product.getName().toLowerCase().startsWith(nameQuery.toLowerCase()));
    }


}
