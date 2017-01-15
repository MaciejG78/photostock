package pl.com.bottega.photostock.sales.model.client;

import pl.com.bottega.photostock.sales.model.client.Address;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.ClientStatus;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Collection;

/**
 * Created by macie on 17.12.2016.
 */
public class VipClient extends Client {

    private Money creditLimit;

    public VipClient(String name, Address address, Money balance, Money creditLimit) {
        super(name, address, ClientStatus.VIP, balance);
        this.creditLimit = creditLimit;
    }

    public VipClient(String number, String name, Address address, Money balance, Money creditLimit, boolean active, Collection<Transaction> transactions) {
        super(number, name, address, ClientStatus.VIP, balance, active, transactions);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean canAfford(Money money) {//czy mogę kupić jako klient bo mam kasę czy jako VIP bo mam kredyt
        return balance.add(creditLimit).gte(money);
    }

    public Money getCreditLimit() {
        return creditLimit;
    }
}
