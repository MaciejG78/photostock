package pl.com.bottega.photostock.sales.infrastructure;

import pl.com.bottega.photostock.sales.model.Address;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.ClientRepository;
import pl.com.bottega.photostock.sales.model.VipClient;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by macie on 08.01.2017.
 */
public class InMemoryClientRepository implements ClientRepository{

    private static final Map<String, Client> REPOSITORY = new HashMap<>();

    static {
        Client client = new Client("Jonny X", new Address(), Money.valueOf(100)); //Klient ma 100 credit-ów
        Client vipClient = new VipClient("Jonny VIP", new Address(), Money.ZERO, Money.valueOf(100));

        REPOSITORY.put(client.getNumber(), client);
        REPOSITORY.put(vipClient.getNumber(),vipClient);
    }

    @Override
    public Client get(String clientNumber){
        return REPOSITORY.get(clientNumber);
    }


}
