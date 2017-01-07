package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.module.money.Rational;
import pl.com.bottega.photostock.sales.module.money.*;

/**
 * Created by macie on 01.01.2017.
 */
public class MoneyTest {

    public static void main(String[] args) {

        Money.Currency DEFAULT_CURRENCY = Money.Currency.CREDIT;

        IntegerMoney money1 = new IntegerMoney(0, DEFAULT_CURRENCY);
        IntegerMoney money2 = new IntegerMoney(100, DEFAULT_CURRENCY);
        IntegerMoney money3 = new IntegerMoney(150, DEFAULT_CURRENCY);
        IntegerMoney money4 = new IntegerMoney(100, DEFAULT_CURRENCY);
        RationalMoney money5 = new RationalMoney(Rational.valueOf(0), DEFAULT_CURRENCY);
        RationalMoney money6 = new RationalMoney(Rational.valueOf(1), DEFAULT_CURRENCY);
        RationalMoney money7 = new RationalMoney(Rational.valueOf(3, 2), DEFAULT_CURRENCY);
        RationalMoney money8 = new RationalMoney(Rational.valueOf(2), DEFAULT_CURRENCY);
        RationalMoney money9 = new RationalMoney(Rational.valueOf(2), DEFAULT_CURRENCY);
        RationalMoney money10 = new RationalMoney(Rational.valueOf(1), DEFAULT_CURRENCY);

        /* TESTY:
        new IntegerMoney(0, currency).equals(new RationalMoney(Rational.valueOf(0), currency));
        new IntegerMoney(100, currency).equals(new RationalMoney(Rational.valueOf(1), currency));
        new IntegerMoney(150, currency).equals(new RationalMoney(Rational.valueOf(3, 2), currency));
        new RationalMoney(Rational.valueOf(2), currency).equals(new RationalMoney(Rational.valueOf(2), currency));
        new IntegerMoney(100, currency).equals(new IntegerMoney(100, currency));
        new RationalMoney(Rational.valueOf(1), currency).equals(new IntegerMoney(100, currency));
        new RationalMoney(Rational.valueOf(3,2), currency).equals(new IntegerMoney(150, currency));
         */

        System.out.println("Positive:");
        System.out.println(money1.equals(money5));
        System.out.println(money2.equals(money6));
        System.out.println(money3.equals(money7));
        System.out.println(money8.equals(money9));
        System.out.println(money2.equals(money4));
        System.out.println(money10.equals(money4));
        System.out.println(money7.equals(money3));
    }


}
