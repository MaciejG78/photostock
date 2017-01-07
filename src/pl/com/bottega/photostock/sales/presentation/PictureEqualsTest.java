package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.module.Clip;
import pl.com.bottega.photostock.sales.module.money.Money;
import pl.com.bottega.photostock.sales.module.Picture;
import pl.com.bottega.photostock.sales.module.Product;

import java.util.HashSet;

/**
 * Created by macie on 11.12.2016.
 * Test zdjęć z Picture
 */
public class PictureEqualsTest {

    public static void main(String[] args) {

        Product product1 = picture("123");
        Product product2 = picture(null);
        Product product3 = picture("123");
        Product product4 = picture("066");
        Product product5 = picture(null);

        Clip clip1 = clip("123");
        Clip clip2 = clip("4");
        Clip clip3 = clip("4");

        System.out.println("Positive:");
        System.out.println(product1.equals(product3));
        System.out.println(product3.equals(product1));
        System.out.println(product1.equals(product1));
        System.out.println(product2.equals(product5));
        System.out.println(clip2.equals(clip3));

        System.out.println("Negative:");
        System.out.println(product1.equals(product2));
        System.out.println(product3.equals(product4));
        System.out.println(product2.equals(product4));
        System.out.println(product1.equals(null)); //Warunek sprawdzający: other == null || !(other instanceof Picture)
        System.out.println(product1.equals(13)); //Warunek sprawdzający: other == null || !(other instanceof Picture)
        System.out.println(product1.equals(clip1));
    }

    private static Picture picture(String number){
        return new Picture(number, "", new HashSet<>(), Money.valueOf(100));
    }

    private static Clip clip(String number){
        return new Clip(number, "", 500L, Money.valueOf(1));
    }
}
