package pl.com.bottega.photostock.sales.module;

import java.util.*;

/**
 * Created by macie on 10.12.2016.
 */
public class Purchase {

    private Client client;
    private Date purchaseDate;
    private List<Product> items;

    public Purchase(Client client, Collection<Product> items) {
        this.client = client;
        this.items = new LinkedList<>(items);
        sortProductsByNumberAsc();
    }


    public Purchase(Client client, Product... items) { //... oznacza dynamiczną liczbę parametrów przekazywanych poprzez konstruktor
        this(client, Arrays.asList(items)); //tworzymy zbiór który będzie zawierał wszystkie item-y (obrazki). HasSet nie przyjmuje tablicy tylko kolekcje, więc musimy zamienić tablicę na kolekcję.
    }

    public int getItemsCount() {
        return items.size();
    }

    public void sortProductsByNumberAsc() {
        this.items.sort(new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                String number1 = p1.getNumber();
                String number2 = p2.getNumber();

                return number1.compareTo(number2); //Domyslne sortowanie rosnąco, żeby wyszło malejące trzeba dodać "-" np.  -number1.compareTo(number2); lub zapis odwrotny number2.compareTo(number1)
            }
        });
    }
}
