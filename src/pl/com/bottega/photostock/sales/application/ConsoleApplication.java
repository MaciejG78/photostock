package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.module.*;
import pl.com.bottega.photostock.sales.module.money.Money;

/**
 * Created by macie on 10.12.2016.
 */
public class ConsoleApplication {

    public static void main(String[] args) {
        ProductRepository productRepository = new InMemoryProductRepository();
        Product product1 = productRepository.get("1");
        Product product2 = productRepository.get("2");
        Product product3 = productRepository.get("3");
        Product product4 = productRepository.get("4");
        Product product5 = productRepository.get("5");
        Product product6 = productRepository.get("6");
        Product product7 = productRepository.get("7");
        Product product8 = productRepository.get("8");


        Client client = new Client("Jonny X", new Address(), Money.valueOf(100)); //Klient ma 100 credit-ów
        Client vipClient = new VipClient("Jonny VIP", new Address(), Money.ZERO, Money.valueOf(100));

        System.out.println(client.introduce());
        System.out.println(vipClient.introduce());

        Reservation reservation = new Reservation(vipClient);

        reservation.add(product1);
        reservation.add(product2);
        reservation.add(product3);
        reservation.add(product4);
        reservation.add(product5);
        reservation.add(product6);
        reservation.add(product7);
        reservation.add(product8);

        System.out.println("After adding items count: " + reservation.getItemsCount());

        Offer offer = reservation.generateOffer();

        boolean canAfford = vipClient.canAfford(offer.getTotalCost());
        System.out.println("Client can afford: " + canAfford);

        if(canAfford) {
            vipClient.charge(offer.getTotalCost(), "Test purchase");
            Purchase purchase = new Purchase(vipClient, product1, product2, product3);
            System.out.println("Client purchased: " + purchase.getItemsCount() + " pictures.");
            System.out.println("Total cost: " + offer.getTotalCost());
        }
        else {
            System.out.println("Client cannot afford");
        }
    }
}
