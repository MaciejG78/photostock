package pl.com.bottega.photostock.sales.module;

/**
 * Created by macie on 17.12.2016.
 */
public class VipClient extends Client{

    private Money creditLimit;

    public VipClient(String name, Address address, Money balance, Money creditLimit){
        super(name, address, ClientStatus.VIP, balance);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean canAfford(Money money) {//czy mogę kupić jako klient bo mam kasę czy jako VIP bo mam kredyt
        return balance.add(creditLimit).gte(money);
    }
}
