package pl.com.bottega.photostock.sales.model.client;

/**
 * Created by macie on 08.01.2017.
 */
public interface ClientRepository {

    Client get(String clientNumber);

    void update(Client client);
}
