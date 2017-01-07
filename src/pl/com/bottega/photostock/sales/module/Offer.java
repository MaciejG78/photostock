package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.money.Money;

import java.util.*;

/**
 * Created by macie on 10.12.2016.
 */
public class Offer {

    private List<Product> items;
    private Client client;

    public Offer(Client client, Collection<Product> items) {
        this.client = client;
        this.items = new LinkedList<Product>(items);
        sortProductsByPriceDesc();
    }

    public boolean sameAs(Offer ofther, Money money) {
        return false;
    }

    public int getItemsCount() {
        return items.size();
    }

    //Częsty schemat w programowaniu, iterowanie po kolekcji i coś robimy na niej.
    public Money getTotalCost() {
        Money totalCost = Money.ZERO;
        for (Product product : items) {
            Money productCost = product.calculatePrice(client);
            totalCost = totalCost.add(productCost);
        }
        return totalCost;
    }

    private void sortProductsByPriceDesc() {
        this.items.sort(new Comparator<Product>() {
            @Override     //Comparator tworzymy przy pomocy anonimowej klasy wewnętrznej
            public int compare(Product p1, Product p2) {
                Money price1 = p1.calculatePrice(client);
                Money price2 = p2.calculatePrice(client);
                //Sortowanie malejące
                return price2.compareTo(price1);
            }
        });
    }

}
