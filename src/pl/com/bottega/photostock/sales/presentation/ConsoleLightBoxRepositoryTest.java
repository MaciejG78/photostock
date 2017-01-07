package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.module.*;
import pl.com.bottega.photostock.sales.module.money.Money;

import java.util.Collection;


/**
 * Created by macie on 13.12.2016.
 */
public class ConsoleLightBoxRepositoryTest {

    public static void main(String[] args) {

        ProductRepository productRepository = new InMemoryProductRepository();

        //tworzy dwóch klientow
        Client client1 = new Client("Jonny X", new Address(), Money.valueOf(100)); //Klient ma 100 credit-ów
        Client client2 = new Client("Gregory Y", new Address(), Money.valueOf(80)); //Klient ma 100 credit-ów

        //tworzy instancję InMemoryLightBoxRepository
        InMemoryLightBoxRepository inMemoryLightBoxRepository = new InMemoryLightBoxRepository();

        //tworzy kilka produktów (jak robiliśmy na zajęciach)
        Product product1 = productRepository.get("1");
        Product product2 = productRepository.get("2");
        Product product3 = productRepository.get("3");
        Product product4 = productRepository.get("4");
        Product product5 = productRepository.get("5");
        Product product6 = productRepository.get("6");

        //tworzy kilka LightBoxów dla obydwu klientów
        LightBox lightBox1 = new LightBox(client1, "Samochody");
        LightBox lightBox2 = new LightBox(client1, "BMW");
        LightBox lightBox3 = new LightBox(client2, "Wyścigowe samochody");
        LightBox lightBox4 = new LightBox(client2, "Samochody elektryczne");
        LightBox lightBox5 = new LightBox(client2, "Cabrio");

        //dodaje do LightBoxów produkty
        lightBox1.add(product1);
        lightBox1.add(product2);
        lightBox1.add(product3);

        lightBox2.add(product1);
        lightBox2.add(product6);

        lightBox3.add(product2);
        lightBox3.add(product4);
        lightBox3.add(product3);

        lightBox4.add(product5);
        lightBox4.add(product6);

        lightBox5.add(product1);

        //zapisuje LightBoxy w repozytorium
        inMemoryLightBoxRepository.put(lightBox1);
        inMemoryLightBoxRepository.put(lightBox2);
        inMemoryLightBoxRepository.put(lightBox3);
        inMemoryLightBoxRepository.put(lightBox4);
        inMemoryLightBoxRepository.put(lightBox5);

        //odczytuje LightBoxy z repozytorium i printuje je po odczytaniu z konsoli
        printLightBoxes(inMemoryLightBoxRepository.getFor(client1));
        printLightBoxes(inMemoryLightBoxRepository.getFor(client2));
    }

    private static void printLightBoxes(Collection<LightBox> lightBoxes) {
        int nr = 1;
        for (LightBox lightBox : lightBoxes) {
            System.out.println(String.format("%d.%s - %s", nr, lightBox.getName(), lightBox.getOwner().getName()));
            printLightBox(lightBox);
            nr++;
        }
    }

    private static void printLightBox(LightBox lightBox) {
        for (Product product : lightBox) {
            System.out.println(String.format("%s %s | %s",
                    (product.isActive() ? "" : "X "),
                    product.getNumber(),
                    product.calculatePrice(lightBox.getOwner())));

        }
    }
}
