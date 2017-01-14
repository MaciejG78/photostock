package pl.com.bottega.photostock.sales.model.product;

import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.money.Money;

/**
 * Created by macie on 17.12.2016.
 */
public class Clip extends AbstractProduct {

    private static final Long FIVE_MINUTES = 1000L * 60 * 5;
    private Long length;

    //Konstruktor
    public Clip(String number, String name, Long length, Money catalogPrice, boolean active) {
        super(name, catalogPrice, number, active);
        this.length = length;
    }

    public Clip(String number, String name, Long length, Money catalogPrice) {
        this(number, name, length, catalogPrice, true);        //ta konstrukcja odwołuje się do głównego konstruktora
    }

    @Override
    public Money calculatePrice(Client client) {
        if (length > FIVE_MINUTES) //Jeśli dłuższy niż 5 minut
            return catalogPrice.multiply(2);
        else
            return catalogPrice;
    }
}

