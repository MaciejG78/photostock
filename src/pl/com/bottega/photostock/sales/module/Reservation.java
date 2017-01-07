package pl.com.bottega.photostock.sales.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by macie on 10.12.2016.
 */
public class Reservation {

    private Client client;
    private Collection<Product> items;

    public Reservation(Client client) {
        this.client = client;
        this.items = new LinkedList<>();
    }

    public void add(Product product) {
        if (items.contains(product))
            throw new IllegalArgumentException(String.format("Product %s is already in this reservation", product.getNumber()));
        product.ensureAvailable();
        items.add(product);
    }


    public void remove(Product product) {
        if (!items.contains(product))
            throw new IllegalArgumentException(String.format("Product %s is not added to this reservation", product.getNumber()));
        items.remove(product);
    }

    //Do zapamiętania schemat
    public Offer generateOffer() {
        Collection<Product> activeItems1 = getActiveItems();
        return new Offer(client, getActiveItems());
    }

    private Collection<Product> getActiveItems() {
        Collection<Product> activeItems = new HashSet<>(); //redukujemy elementy kolekcji do mniejszej kolekcji odfiltrowanej
        for (Product product : items) {
            if (product.isActive())
                activeItems.add(product);
        }
        return activeItems;
    }

    public int getItemsCount() {
        return items.size();
    }
}
