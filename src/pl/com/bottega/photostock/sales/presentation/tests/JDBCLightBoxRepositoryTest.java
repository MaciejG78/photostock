package pl.com.bottega.photostock.sales.presentation.tests;

import pl.com.bottega.photostock.sales.infrastructure.jdbc.JDBCClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.jdbc.JDBCLightBoxRepository;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.ClientRepository;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.lightbox.LightBoxRepository;
import pl.com.bottega.photostock.sales.model.money.Money;
import pl.com.bottega.photostock.sales.model.product.Picture;
import pl.com.bottega.photostock.sales.model.product.Product;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by macie on 22.01.2017.
 */
public class JDBCLightBoxRepositoryTest {

    public static void main(String[] args) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9001/photostock", "SA", "");

        LightBoxRepository lightBoxRepository = new JDBCLightBoxRepository(c);
        ClientRepository clientRepository = new JDBCClientRepository(c);

        Client client = clientRepository.get("200");
        System.out.println(client.getNumber() + ", " + client.getName());

        Collection<String> lightBox = lightBoxRepository.getLightBoxNames(client);

        System.out.println(lightBox);

        Collection<LightBox> lightBoxes = lightBoxRepository.getFor(client);
        for (LightBox oneLightBox : lightBoxes) {
            System.out.println("-------------------");
            System.out.println(oneLightBox.getName());
            System.out.println("-------------------");
            Collection<Product> product = oneLightBox.getProducts();
            for (Product oneProduct : product)
                System.out.println(oneProduct.getName());
        }

        System.out.println("---------------------------------------");
        System.out.println("Zawartość lightboxa o nazwie: auta");
        LightBox clientLightBox = lightBoxRepository.findLightBox(client, "auta");
        for (Product lb : clientLightBox) {
            System.out.println(lb.getName());
        }

        lightBoxRepository.put(lightBoxRepository.findLightBox(client, "wodoloty"));
        System.out.println("---------------------------------------");
        clientLightBox.add(new Picture("14", "Skoda Superb", new ArrayList<String>(), Money.valueOf(14)));
        clientLightBox.add(new Picture("15", "Szybowiec", new ArrayList<String>(), Money.valueOf(100)));
        for (Product lb : clientLightBox) {
            System.out.println(lb.getName() + ", " + lb.getNumber());
        }
        System.out.println("---------------------------------------");
        System.out.println("Dodajemy nowego lightboxa (jak nie ma)");

        lightBoxRepository.put(clientLightBox);
    }
}
