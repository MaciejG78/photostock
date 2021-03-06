package pl.com.bottega.photostock.sales.model.money;

/**
 * Created by macie on 18.12.2016.
 */
public interface Money extends Comparable<Money> {

    Currency DEFAULT_CURRENCY = Currency.CREDIT;

    enum Currency {CREDIT;}

    Money ZERO = valueOf(0, DEFAULT_CURRENCY);

    Money add(Money add);

    Money subtract(Money subtrahend);

    Money multiply(long factor);

    default boolean gte(Money other) {
        return compareTo(other) >= 0;
    }

    default boolean gt(Money other) {
        return compareTo(other) > 0;
    }

    default boolean lte(Money other) {
        return compareTo(other) <= 0;
    }

    default boolean lt(Money other) {
        return compareTo(other) < 0;
    }

    Money opposite();

    RationalMoney convertToRational();

    IntegerMoney convertToInteger();

    static Money valueOf(long cents, Currency currency) {
        return new IntegerMoney(cents, currency);
    }

    static Money valueOf(long cents) {
        return new IntegerMoney(cents, DEFAULT_CURRENCY);
    }


    static Money valueOf(float value) {
        return new IntegerMoney((long) ((value + 0.001) * 100.0), DEFAULT_CURRENCY);
    }

    //100.00 CREDIT
    static Money valueOf(String moneyString) {
        String[] moneyComponents = moneyString.split(" ");
        if (moneyComponents.length != 1 && moneyComponents.length != 2)
            throw new IllegalArgumentException("Inwalid money format");
        long value = (long) (Double.parseDouble(moneyComponents[0]) * 100.0);
        if (moneyComponents.length == 2){
            return  new IntegerMoney(value, Currency.valueOf(moneyComponents[1]));
        }
        return new IntegerMoney(value, DEFAULT_CURRENCY);
    }
    /*
    static Money valueOf(long value, Currency currency) {
        return new RationalMoney(Rational.valueOf(value), currency);
    }

    static Money valueOf(Rational value, Currency currency) {
        return new RationalMoney(value, currency);
    }

    static Money valueOf(long value) {
        return new RationalMoney(Rational.valueOf(value), DEFAULT_CURRENCY);
    }

*/


}
