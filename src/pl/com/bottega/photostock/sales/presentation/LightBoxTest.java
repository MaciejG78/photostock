package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.module.*;
import pl.com.bottega.photostock.sales.module.money.Money;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by macie on 11.12.2016.
 */
public class LightBoxTest {

    public static void main(String[] args) {

        ProductRepository productRepository = new InMemoryProductRepository();

        Collection<String> tags = Arrays.asList("przyroda", "motoryzacja");
        Product product1 = productRepository.get("1");
        Product product2 = productRepository.get("2");
        Product product3 = productRepository.get("3");
        Product product4 = productRepository.get("4");
        Product product5 = productRepository.get("5");
        Product product6 = productRepository.get("6");

        Client client1 = new Client("Jonny X", new Address(), Money.valueOf(100)); //Klient ma 100 credit-ów
        Client client2 = new Client("Danny X", new Address(), Money.valueOf(80)); //Klient ma 100 credit-ów

        LightBox lightBox1 = new LightBox(client1, "Samochody");
        LightBox lightBox2 = new LightBox(client1, "BMW");
        LightBox lightBox3 = new LightBox(client2, "Wyścigowe samochody");


        lightBox1.add(product1);
        lightBox1.add(product2);
        lightBox1.add(product3);

        lightBox2.add(product1);

        lightBox3.add(product3);

        product1.deactivate();

        printLightBoxes(lightBox1, lightBox2, lightBox3);

        LightBox lightBox = LightBox.joined(client1, "Joined lightBox", lightBox1, lightBox2, lightBox3);
        System.out.println("Joined: ");
        printLightBox(lightBox);

    }

    public static void printLightBoxes(LightBox ... lightBoxes) {
        int nr = 1;
        for (LightBox lightBox : lightBoxes){
            System.out.println(String .format("%d.%s - %s", nr, lightBox.getName(), lightBox.getOwner().getName()));
            printLightBox(lightBox);
            nr++;
        }
    }

    public static void printLightBox(LightBox lightBox) {
        for(Product product : lightBox){ //żeby zadzałała pętla automatyczna należy zaimplementować w LightBoxie
                                        // Iterable<Picture> i metodę iteratora
            System.out.println(String.format("%s %s | %s", //X, numer zdjęcia | cena
                    (product.isActive() ? "" : "X "),
                    product.getNumber(),
                    product.calculatePrice(lightBox.getOwner())));

        }

    }

}
