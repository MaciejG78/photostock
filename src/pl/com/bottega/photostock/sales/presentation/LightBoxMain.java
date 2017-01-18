package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.application.AuthenticationProcess;
import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.application.ProductCatalog;
import pl.com.bottega.photostock.sales.application.PurchaseProcess;
import pl.com.bottega.photostock.sales.infrastructure.csv.CSVClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.csv.CSVLightBoxRepository;
import pl.com.bottega.photostock.sales.infrastructure.memory.*;
import pl.com.bottega.photostock.sales.model.client.ClientRepository;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.product.ProductRepository;
import pl.com.bottega.photostock.sales.model.purchase.PurchaseRepository;
import pl.com.bottega.photostock.sales.model.purchase.ReservationRepository;

import java.util.Scanner;

/**
 * Created by macie on 07.01.2017.
 */
public class LightBoxMain {

    private MainScreen mainScreen;
    private SearchScreen searchScreen;
    private ReservationScreen reservationScreen;
    private OfferScreen offerScreen;
    private LoginScreen loginScreen;
    private LightBoxScreen lightBoxScreen;

    public LightBoxMain() {
        Scanner scanner = new Scanner(System.in);
        ProductRepository productRepository = new InMemoryProductRepository();
        ProductCatalog productCatalog = new ProductCatalog(productRepository);
        ClientRepository clientRepository = new CSVClientRepository("C:\\Bottega_Java\\Projekty\\photostock\\data");
        AuthenticationProcess authenticationProcess = new AuthenticationProcess(clientRepository);
        ReservationRepository reservationRepository = new InMemoryReservationRepository();
        PurchaseRepository purchaseRepository = new InMemoryPurchaseRepository();
        PurchaseProcess purchaseProcess = new PurchaseProcess(clientRepository, reservationRepository, productRepository, purchaseRepository);
        LightBoxRepository lightBoxRepository = new CSVLightBoxRepository("C:\\Bottega_Java\\Projekty\\photostock\\data", productRepository);
        LightBoxManagement lightBoxManagement = new LightBoxManagement(purchaseProcess, lightBoxRepository, productRepository, clientRepository);
        loginScreen = new LoginScreen(scanner, authenticationProcess);
        searchScreen = new SearchScreen(scanner, productCatalog, loginScreen);
        reservationScreen = new ReservationScreen(scanner, loginScreen, purchaseProcess);
        offerScreen = new OfferScreen(scanner, loginScreen, purchaseProcess);
        lightBoxScreen = new LightBoxScreen(scanner, loginScreen, lightBoxManagement);
        mainScreen = new MainScreen(scanner, searchScreen, reservationScreen, offerScreen, lightBoxScreen);
    }

    public void start() {
        loginScreen.print();
        mainScreen.print();
    }

    public static void main(String[] args) {
        new LightBoxMain().start();

    }
}
