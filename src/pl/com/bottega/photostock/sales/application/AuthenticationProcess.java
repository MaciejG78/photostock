package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.ClientRepository;

/**
 * Created by macie on 08.01.2017.
 */
public class AuthenticationProcess {
    private ClientRepository clientRepository;

    public AuthenticationProcess(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public Client authenticate(String clientNumber){
        return clientRepository.get(clientNumber);
    }
}
