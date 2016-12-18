package pl.com.bottega.photostock.sales.module;

/**
 * Created by macie on 10.12.2016.
 */
public class Money implements Comparable<Money>{ //Comparable - interfejs służy fo porównywania obiektów

    private static final Currency DEFAULT_CURRENCY = Currency.CREDIT;

    public static final Money ZERO = valueOf(0, DEFAULT_CURRENCY);

    public enum Currency {PLN, EUR, USD, CREDIT;}

    public static Money valueOf(Rational value, Currency currency) {
        return new Money(value, currency);
    }

    public static Money valueOf(long value, Currency currency) {
        return new Money(Rational.valueOf(value), currency);
    }

    public static Money valueOf(long value) {
        return new Money(Rational.valueOf(value), DEFAULT_CURRENCY);
    }

    public Money add(Money addend) {
        if (currency != addend.currency)
            throw new IllegalArgumentException("The currencies do not match.");

        return valueOf(value.add(addend.value), currency);
    }

    public Money subtract(Money subtrahend) {
        if (currency != subtrahend.currency)
            throw new IllegalArgumentException("The currencies do not match.");

        return valueOf(value.subtract(subtrahend.value), currency);
    }

    public Money multiply(long factor) {
        if (factor < 0)
            throw new IllegalArgumentException("Money cannot be negative");

        return valueOf(value.multiply(factor), currency);
    }

    public Money opposite() {
        return valueOf(value.negative(), currency);
    }

    public boolean gte(Money other){
        return compareTo(other) >= 0;
    }

    public boolean gt(Money other){
        return compareTo(other) > 0;
    }

    public boolean lte(Money other){
        return compareTo(other) <= 0;
    }

    public boolean lt(Money other){
        return compareTo(other) < 0;
    }

    public int compareTo(Money other){
        if (!other.currency.equals(currency))
            throw new IllegalArgumentException("Currency missmatch"); //tego wyjątku nie trzeba deklarować poprzez throws jest to wyjątek Runtimowy
        return value.compareTo(other.value);
    }

    private final Rational value;
    private final Currency currency;

    private Money(Rational value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return value.toDouble() + " " + currency.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;

        Money money = (Money) o;

        if (!value.equals(money.value)) return false;
        return currency == money.currency;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }
}
