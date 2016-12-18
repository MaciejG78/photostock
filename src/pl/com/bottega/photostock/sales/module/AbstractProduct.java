package pl.com.bottega.photostock.sales.module;

import pl.com.bottega.photostock.sales.module.money.Money;

/**
 * Created by macie on 17.12.2016.
 */
public abstract class AbstractProduct implements Product {
    protected String number;
    private String name;
    protected Money catalogPrice;
    private boolean active;
    private Client reservationOwner;
    private Client buyer;

    public AbstractProduct(String name, Money catalogPrice, String number, boolean active) {
        this.name = name;
        this.catalogPrice = catalogPrice;
        this.number = number;
        this.active = active;
    }

    public abstract Money calculatePrice(Client client);

    @Override
    public boolean isAvailable(){
        return active && !isSold() && !isReserved();
    }

    private boolean isReserved() {
        return reservationOwner != null;
    }

    private boolean isSold() {
        return buyer != null;
    }

    @Override
    public void reservedPer(Client client){
        ensureAvailable();
        reservationOwner = client;
    }

    @Override
    public void unreservedPer(Client client){
        ensureReservedBy(client);
        reservationOwner = null;
    }

    private void ensureReservedBy(Client client) {
        if(!isReservedBy(client))
            throw new IllegalArgumentException(String.format("Client &s is not reserved by %s", getNumber(), client.getName));
    }

    private boolean isReservedBy(Client client){
        return isReserved() && client.equals(reservationOwner);
    }

    @Override
    public void soldPer(Client client){
        ensureReservedBy(client);
        buyer = client;
        unreservedPer(client);
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean deactivate() {
        return active = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractProduct product = (AbstractProduct) o;

        return number != null ? number.equals(product.number) : product.number == null;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }


//metoda equals napisany przez nas ręcznie
//    @Override
//    public boolean equals(Object other){ //Musi być Object bo ma się wykonać na obiekcie a nie ma być przeciążana np. (Picture other)
//        if (this == other) return true;
//        if(other == null || !(other instanceof Picture))
//            return false;
//        Picture otherPicture = (Picture) other;
//        return ((otherPicture.number == this.number) || otherPicture.number != null && otherPicture.number.equals(this.number));
//    }

}
