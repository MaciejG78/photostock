package pl.com.bottega.photostock.sales.model.client;

import pl.com.bottega.photostock.sales.model.money.Money;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by macie on 10.12.2016.
 */
public class Client {

    private static int clientNumber;

    private String name;
    private Address address;
    private ClientStatus status;

    protected Money balance;
    private Collection<Transaction> transactions;
    private boolean active;
    private String number;

    //public Object getName;

    public Client(String name, Address address, ClientStatus status, Money initialbalance) {
        this(nextNumber(), name, address, status, initialbalance, true, new LinkedList<>());
        if (!initialbalance.equals(Money.ZERO))
            this.transactions.add(new Transaction(initialbalance, "Openning account."));
    }

    public Client(String number, String name, Address address, ClientStatus status,
                  Money initialbalance, boolean active, Collection<Transaction> transactions) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.balance = initialbalance;
        this.active = active;
        this.transactions = new LinkedList<>(transactions);
        this.number = number;
    }

    private static String nextNumber() {
        clientNumber += 100;
        return String.valueOf(clientNumber);
    }

    public Client(String name, Address address, Money balance) {
        this(name, address, ClientStatus.STANDARD, balance);
    }

    public boolean canAfford(Money money) {
        return balance.gte(money); //gte() - greather then equal - metoda będzie sprawdzała czy są środki
    }

    //Płatność za zakupy
    public void charge(Money money, String reason) {
        if (money.lte(Money.ZERO))
            throw new IllegalArgumentException("Negative charge amount prohibited");
        if (canAfford(money)) {
            Transaction chargeTransaction = new Transaction(money.opposite(), reason); //opposite - liczba przeciwna
            transactions.add(chargeTransaction);
            balance = balance.subtract(money);
        } else {
            String template = "Client balance is %s and requested amount was %s"; //Formatowanie stringów
            String message = String.format(template, balance, money);
            throw new CantAffordException(message); //Można w jednej linijce: throw new CantAffordException(String.format("Client balance is %s and requested amount was %s", balance, money));
        }

    }

    //Doładowanie konta o nowe kredyty
    public void recharge(Money money) {
        if (money.lte(Money.ZERO))
            throw new IllegalArgumentException("Negative recharge amount prohibited");
        Transaction transaction = new Transaction(money, "Recharge account");
        transactions.add(transaction);
        balance.add(money);

    }

    public String getName() {
        return name;
    }

    public String introduce() {
        String statusName = status.getStatusName();
        return String.format("%s - %s", name, statusName);
    }

    public void deactivate(){
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public String getNumber(){
        return number;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public Money getBalance() {
        return balance;
    }

    public Collection<Transaction> getTransactions() {
        return  new LinkedList<>(transactions);
    }
}
