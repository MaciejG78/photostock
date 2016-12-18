package pl.com.bottega.photostock.sales.module.money;

/**
 * Created by macie on 18.12.2016.
 */
public interface Money extends Comparable<Money>{

    Currency DEFAULT_CURRENCY = Currency.CREDIT;

    enum Currency {CREDIT;}

    Money ZERO = valueOf(0, DEFAULT_CURRENCY);

    Money add(Money add);

    Money subtract(Money subtrahend);

    Money multiply(long factor);

    boolean gte(Money other);

    boolean gt(Money other);

    boolean lte(Money other);

    boolean lt(Money other);

    Money opposite();

    RationalMoney convertToRational();

    static Money valueOf(long value, Currency currency) {
        return new RationalMoney(Rational.valueOf(value), currency);
    }

    static Money valueOf(Rational value, Currency currency) {
        return new RationalMoney(value, currency);
    }

    static Money valueOf(long value) {
        return new RationalMoney(Rational.valueOf(value), DEFAULT_CURRENCY);
    }




}
