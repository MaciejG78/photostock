package pl.com.bottega.photostock.sales.module;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by macie on 10.12.2016.
 */
public class LightBox implements Iterable<Product>{

    private String name;
    private Client client;
    private Collection<Product> items = new LinkedList<>();


    public LightBox(Client client, String name){
        this.client = client;
        this.name = name;
    }

    public void add(Product product){
        if(items.contains(product))
            throw new IllegalArgumentException(String.format("LightBox already contain this picture %s", product.getNumber()));
        product.ensureAvailable();
        items.add(product);

    }


    public void remove(Product product){
        if(items.contains(product))
            throw new IllegalArgumentException(String.format("LightBox not contain picture %s", product.getNumber()));
        items.remove(product);
    }

    public void rename(String newName){
        this.name = newName;
    }

    @Override
    public Iterator<Product> iterator() {
        return items.iterator(); //Każda kolekcja implementuje iterator więc nie musimy od początku implementować
    }

    public String getName() {
        return name;
    }

    public Client getOwner() {
        return client;
    }

    public static LightBox joined(Client client, String name, LightBox ... lightBoxes) {
        LightBox output = new LightBox(client, name);
        for(LightBox lightBox : lightBoxes){
            for(Product product : lightBox){
                if (!output.items.contains(product) && product.isActive())
                output.items.add(product); //można napisać krócej output.add(picture); ale wtedy ponownie wykonywana byłąby walidacja

            }
        }

            return output;
    }
}