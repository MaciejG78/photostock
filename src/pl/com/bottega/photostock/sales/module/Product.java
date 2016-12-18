package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.money.Money;

/**
 * Created by macie on 17.12.2016.
 */
public interface Product {
    Money calculatePrice(Client client);

    boolean isAvailable();

    void reservedPer(Client client);

    void unreservedPer(Client client);

    void soldPer(Client client);

    String getNumber();

    String getName();

    boolean isActive();

    boolean deactivate();

    //Jeśli metoda jest default może być przeniesiona do interfejsu, nie może korzystać z pól
    default void ensureAvailable(){
        if(!isAvailable())
            throw new ProductNotAvailableException(this);
    }
}
