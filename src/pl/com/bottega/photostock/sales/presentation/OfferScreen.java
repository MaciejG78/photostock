package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.application.PurchaseProcess;
import pl.com.bottega.photostock.sales.module.Offer;
import pl.com.bottega.photostock.sales.module.Product;

import java.util.Scanner;

/**
 * Created by macie on 07.01.2017.
 */
public class OfferScreen {

    private Scanner scanner;
    private final LoginScreen loginScreen;
    private ReservationScreen reservationScreen;
    private PurchaseProcess purchaseProcess;

    public OfferScreen(Scanner scanner, LoginScreen loginScreen, PurchaseProcess purchaseProcess){
        this.scanner = scanner;
        this.loginScreen = loginScreen;
        this.purchaseProcess = purchaseProcess;
    }

    public void print() {
        String reservationNumber = purchaseProcess.getReservation(loginScreen.getAuthenticatedClientNumber());
        try {
            Offer offer = purchaseProcess.calculateOffer(reservationNumber);
            printOffer(offer);
        }
        catch (IllegalStateException ex){
            System.out.println("Nie ma aktywnych produktów w rezerwacji. Dodaj produkty.");
        }
    }

    private void printOffer(Offer offer){
        System.out.println("Oferta specjalnie dla Ciebie:");
        int i = 1;
        for(Product product : offer.getItems()){
            System.out.println(String.format("%d. %s", i++, product.getName()));
        }
        System.out.println(String.format("Zaledwie: %s", offer.getTotalCost()));
    }


}
