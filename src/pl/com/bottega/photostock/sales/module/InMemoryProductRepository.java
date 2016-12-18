package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.money.Money;

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

        Product  clip1 = new Clip("7", "Wściekłe pięści węża", 2L * 1000 * 60 * 2, Money.valueOf(5));
        Product  clip2 = new Clip("8", "Sum tzw. olimpijczyk", 40L * 1000 * 60 * 2, Money.valueOf(10));

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
    public Product get(String number){
        return REPOSITORY.get(number);
    }


}
