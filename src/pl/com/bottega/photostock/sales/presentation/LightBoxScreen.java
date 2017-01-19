package pl.com.bottega.photostock.sales.presentation;

import pl.com.bottega.photostock.sales.application.LightBoxManagement;
import pl.com.bottega.photostock.sales.infrastructure.csv.CSVTransactionsRepository;
import pl.com.bottega.photostock.sales.model.client.Transaction;
import pl.com.bottega.photostock.sales.model.client.TransactionRepository;
import pl.com.bottega.photostock.sales.model.lightbox.LightBox;
import pl.com.bottega.photostock.sales.model.product.Product;

import java.util.Collection;
import java.util.Scanner;

/**
 * Created by maciek on 07.01.2017.
 * Na w.w. user może wydawać następujące komendy:
 * pokaz -> powoduje wyswietlenie nazw wszystkich lighboxow
 * pokaz [nazwa lightboxa] -> powoduje wyswietlenie produktow znajdujacych sie w lighboxie o zadanej nazwie
 * dodaj [nazwa lightboxa] [nr produktu] -> powoduje dodanie do lightboxa o zadanej nazwie produktu o zadanym numerze
 * powrot -> powoduje powrot do menu głównego aplikacji
 * wpisanie błędnej komendy powoduje wyświetlenie komunikatu -> "Sorry nie rozumiem"
 * <p>
 * Podpowiedź. W metodzie print() klasy LightBoxScreen napisz nieskończoną pętlę która pobiera stringa i patrzy
 * czy w podanym stringu jest któraś z wymienionych komend. Do parsowania stringa użyj metody split() klasy string.
 * Wykonanie każdej z w.w. komend powinno być, zgodnie z regułami arch, warstwowej delegowane do następnej warstwy
 * t.j. do warstwy aplikacji. I tu stwórz klasę LightBoxManagement:
 */
public class LightBoxScreen {

    private Scanner scanner;
    private final LoginScreen loginScreen;
    private final LightBoxManagement lightBoxManagement;
    private TransactionRepository transactionRepository;
    private boolean exit;

    public LightBoxScreen(Scanner scanner, LoginScreen loginScreen, LightBoxManagement lightBoxManagement, TransactionRepository transactionRepository) {
        this.scanner = scanner;
        this.loginScreen = loginScreen;
        this.lightBoxManagement = lightBoxManagement;
        this.transactionRepository = transactionRepository;
    }

    public void print() {
        exit = false;
        while (!exit) {
            printMenu();
            String[] cmd = readCommand();
            executeCommand(cmd);
        }
    }

    private String[] readCommand() {
        return scanner.nextLine().split(" ");
    }

    private void executeCommand(String[] cmd) {
        if (cmd.length == 1) {
            if (cmd[0].equals("pokaz")) {
                showLightBoxes();
                return;
            }
            if (cmd[0].equals("transakcje")) {
                showTransactions();
                return;
            }
            if (cmd[0].equals("powrot")) {
                exit = true;
                return;
            }
        } else if (cmd.length == 2) {
            if (cmd[0].equals("pokaz")) {
                showLightBox(cmd[1]);
                return;
            }
            if (cmd[0].equals("rezerwuj")) {
                reserveLightBox(cmd[1]);
                return;
            }
        } else if (cmd.length == 3 && cmd[0].equals("dodaj")) {
            addToLightBox(cmd[1], cmd[2]);
            return;
        }
        System.out.println("Sorry nie rozumiem ;(");
    }

    private void showTransactions() {
        String clientNumber = loginScreen.getAuthenticatedClientNumber();
        Collection<Transaction> transactions = transactionRepository.getTransactions(clientNumber);
        if (transactions == null) {
            System.out.println("Nie masz aktualnie żadnych transakcji na koncie");
        } else {
            int i = 1;
            for (Transaction transaction : transactions) {
                System.out.println(i + ". Wartość transakcji:" + transaction.getValue() + ", Opis transakcji: " + transaction.getDescription());
                i++;
            }
        }

    }

    private void reserveLightBox(String lightBoxName) {
        lightBoxManagement.reserve(loginScreen.getAuthenticatedClientNumber(), lightBoxName);
        System.out.println("Produkty zostały zarezerwowane");

    }

    private void addToLightBox(String ligthBoxName, String productNumber) {
        try {
            lightBoxManagement.addProduct(
                    loginScreen.getAuthenticatedClientNumber(),
                    ligthBoxName,
                    productNumber
            );
            System.out.println(String.format("Produkt %s został dodany do light boxa %s.",
                    productNumber, ligthBoxName));
        } catch (Exception ex) {
            System.out.println(String.format("Nie udało się dodać produktu %s do light boxa %s. Komunikat błędu: %s",
                    productNumber, ligthBoxName,
                    ex.getMessage()));
        }
    }

    private void showLightBox(String lightBoxName) {
        LightBox lightBox = null;
        try {
            lightBox = lightBoxManagement.getLightBox(loginScreen.getAuthenticatedClientNumber(), lightBoxName);
        } catch (IllegalArgumentException ex) {
            System.out.println(String.format("Nie znaleziono light boxa %s", lightBoxName));
            return;
        }
        System.out.println(String.format("Zawartość light boxa %s", lightBoxName));
        int i = 1;
        for (Product product : lightBox) {
            System.out.println(String.format("%d. %s", i++, product.getName()));
        }
    }

    private void showLightBoxes() {
        Collection<String> lightBoxes = lightBoxManagement.getLightBoxNames(loginScreen.getAuthenticatedClientNumber());
        if (lightBoxes.size() == 0)
            System.out.println("Nie masz aktualnie żadnych light boxów");
        else {
            System.out.println("Twoje light boxy:");
            int i = 1;
            for (String s : lightBoxes) {
                System.out.println(String.format("%d. %s", i++, s));
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Aby wyswietlić nazwy wszystkich lighboxow wpisz słowo: pokaz\n" +
                "2. Aby wyswietlić produkty znajdujace sie w lighboxie o zadanej nazwie wpisz słowo: pokaz [nazwa lightboxa]\n" +
                "3. Aby dodać do lightboxa o zadanej nazwie produkt o zadanym numerze wpisz słowo: dodaj [nazwa lightboxa] [nr produktu]\n" +
                "4. Aby zarezerwować produkty z LightBoxa wpisz słowo: rezerwuj [nazwa lightboxa]\n" +
                "5. Aby pokazać transakcje klienta wpisz słowo: transakcje\n" +
                "6. Aby powrócić do menu głównego aplikacji wpisz słowo: powrot");
    }


}
    /*
    private void executeCommand(String command) {
        String[] commandTab = command.split("\\s");
        if (commandTab.length == 0)
            System.out.println("Wprowadź jakiąś komendę !!!");
        else {
            if (commandTab[0].equals("pokaz") && commandTab.length == 1)
                lightBoxManagement.getLighBoxNames(loginScreen.getAuthenticatedClientNumber());
            else if (commandTab[0].equals("pokaz") && commandTab[1].matches("\\[.*\\]") && commandTab.length == 2)
                System.out.println("Wybrałeś 'pokaz[lightbox]'");
            else if (commandTab[0].equals("dodaj") && commandTab[1].matches("\\[.*\\]") && commandTab[2].matches("\\[.*\\]") && commandTab.length == 3)
                System.out.println("Wybrałeś 'dodaj[lightbox][produkt]'");
            else if (commandTab[0].equals("powrot") && commandTab.length == 1)
                mainScreen.print(); //System.out.println("Wybrałeś 'powrot'");
            else
                System.out.println("Sorry, nie rozumien ;(");
        }
    }

    private String getCommand() {
        System.out.print("Co chcesz zrobić: \n");
        return scanner.nextLine();
    }
*/


