package pl.com.bottega.photostock.sales.model.money;

/**
 * Created by macie on 18.12.2016.
 */
public class IntegerMoney implements Money {

    private long cents;
    public Currency currency;

    public IntegerMoney(long cents, Currency currency) {
        this.cents = cents;
        this.currency = currency;
    }

    @Override
    public Money add(Money add) {
        IntegerMoney integerMoney = safeConvert(add);
        return new IntegerMoney(cents + integerMoney.cents, currency);
    }

    @Override
    public Money subtract(Money subtrahend) {
        IntegerMoney integerMoney = safeConvert(subtrahend);
        return new IntegerMoney(cents - integerMoney.cents, currency);
    }

    @Override
    public Money multiply(long factor) {
        if (factor < 0)
            throw new IllegalArgumentException("RationalMoney cannot be negative");
        return new IntegerMoney(cents * factor, currency);
    }

    @Override
    public Money opposite() {
        return new IntegerMoney(cents, currency);
    }

    @Override
    public RationalMoney convertToRational() {
        Rational rational = Rational.valueOf(cents, 100);
        return new RationalMoney(rational, currency);
    }

    @Override
    public IntegerMoney convertToInteger() {
        return this;
    }

    public int compareTo(Money other) {
        IntegerMoney integerMoney = safeConvert(other);
        if (cents == integerMoney.cents)
            return 0;               //Jeśli równe to 0,
        /* Ten zapis można zapisać w jednej linijce: return cents < integerMoney.cents ? -1 : 1 ;
        else if (cents < integerMoney.cents)
                return -1;
        else
            return 1;
        */
        return cents < integerMoney.cents ? -1 : 1;        //jeśli nie są równe to -1 lub 1
    }

    @Override
    public String toString() {
        return cents / 100 + " " + currency.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegerMoney))
            if (o instanceof RationalMoney) {
                RationalMoney money = (RationalMoney) o;
                if (cents != money.convertToInteger().cents) return false;
                return currency == money.currency;
            } else
                return false;

        IntegerMoney that = (IntegerMoney) o;

        if (cents != that.cents) return false;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        int result = (int) (cents ^ (cents >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    private void ensureSameCurrency(IntegerMoney other) {
        if (currency != other.currency)
            throw new IllegalArgumentException("Currency missmatch");
    }

    private IntegerMoney safeConvert(Money other) {
        IntegerMoney integerMoney = other.convertToInteger();
        ensureSameCurrency(integerMoney);
        return integerMoney;
    }
}
