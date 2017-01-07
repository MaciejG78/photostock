package pl.com.bottega.photostock.sales.module.money;

/**
 * Created by macie on 10.12.2016.
 */

public class RationalMoney implements Money { //Comparable - interfejs służy fo porównywania obiektów

    private final Rational value;
    public final Currency currency;

    public RationalMoney(Rational value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public Money add(Money addend) {
        RationalMoney rationalMoney = addend.convertToRational();
        if (currency != rationalMoney.currency)
            throw new IllegalArgumentException("The currencies do not match.");

        return new RationalMoney(value.add(rationalMoney.value), currency);
    }

    public Money subtract(Money subtrahend) {
        RationalMoney rationalMoney = subtrahend.convertToRational();
        if (currency != rationalMoney.currency)
            throw new IllegalArgumentException("The currencies do not match.");

        return new RationalMoney(value.subtract(rationalMoney.value), currency);
    }

    public Money multiply(long factor) {
        if (factor < 0)
            throw new IllegalArgumentException("RationalMoney cannot be negative");

        return new RationalMoney(value.multiply(factor), currency);
    }

    public Money opposite() {
        return new RationalMoney(value.negative(), currency);
    }

    public int compareTo(Money other){
        RationalMoney rationalMoney = other.convertToRational();
        if (!rationalMoney.currency.equals(currency))
            throw new IllegalArgumentException("Currency missmatch"); //tego wyjątku nie trzeba deklarować poprzez throws jest to wyjątek Runtimowy
        return value.compareTo(rationalMoney.value);
    }


    @Override
    public String toString() {
        return value.toDouble() + " " + currency.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RationalMoney)) {
            if (o instanceof IntegerMoney){
                IntegerMoney money = (IntegerMoney) o;
                if (!value.equals(money.convertToRational().value)) return false;
                return currency == money.currency;
            }
            else
                return false;

        }

      RationalMoney money = (RationalMoney) o;

        if (!value.equals(money.value)) return false;
        return currency == money.currency;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public RationalMoney convertToRational(){
        return this;
    }

    @Override
    public IntegerMoney convertToInteger(){
        long cents = value.getNumerator()*100/value.getDenominator();
        return new IntegerMoney(cents, currency);
    }
}
