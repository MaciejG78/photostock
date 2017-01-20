package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.application.AuthenticationProcess;
import pl.com.bottega.photostock.sales.model.client.Client;
import pl.com.bottega.photostock.sales.model.client.Transaction;
import pl.com.bottega.photostock.sales.model.client.TransactionRepository;

import java.util.Collection;
import java.util.Scanner;

/**
 * Created by macie on 08.01.2017.
 */
public class LoginScreen {

    private Scanner scanner;
    private AuthenticationProcess authenticationProcess;
    private Client client;
    private TransactionRepository transactionRepository;

    public LoginScreen(Scanner scanner, AuthenticationProcess authenticationProcess, TransactionRepository transactionRepository) {
        this.scanner = scanner;
        this.authenticationProcess = authenticationProcess;
        this.transactionRepository = transactionRepository;
    }

    public void print(){
        while(true){
            System.out.println("Podaj numer klienta: ");
            String clientNumber = scanner.nextLine();
            client = authenticationProcess.authenticate(clientNumber);
            if (client != null) {
                System.out.println(String.format("Witaj %s", client.getName()));
                showTransactions();
                return;
            }
            System.out.println("Nieprawidłowy numer klienta. Spróbuj ponownie.");
        }
    }

    public String getAuthenticatedClientNumber(){
        return client.getNumber();
    }

    public Client getClient() {
        return client;
    }

    private void showTransactions() {
        String clientNumber = getClient().getNumber();
        Collection<Transaction> transactions = transactionRepository.getTransactions(clientNumber);
        if (transactions == null) {
            System.out.println("Nie masz aktualnie żadnych transakcji na koncie");
        } else {
            System.out.println("Twoje dotychczasowe transakcje:");
            int i = 1;
            for (Transaction transaction : transactions) {
                System.out.println(i + ". Wartość transakcji:" + transaction.getValue() + ", Opis transakcji: " + transaction.getDescription());
                i++;
            }
        }

    }
}