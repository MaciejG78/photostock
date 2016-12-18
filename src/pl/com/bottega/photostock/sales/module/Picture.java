package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.money.Money;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by macie on 10.12.2016.
 */
public class Picture extends AbstractProduct {


    private Collection<String> tags;

    //Konstruktor
    public Picture(String number, String name, Collection<String> tags, Money catalogPrice, boolean active){
        super(name, catalogPrice, number, active);
        this.tags = new HashSet<String>(tags);
    }

    public Picture(String number, String name, Collection<String> tags, Money catalogPrice){
        this(number, name, tags, catalogPrice, true);        //ta konstrukcja odwołuje się do głównego konstruktora
    }

    @Override
    public Money calculatePrice(Client client){
        return catalogPrice;
    }


}
