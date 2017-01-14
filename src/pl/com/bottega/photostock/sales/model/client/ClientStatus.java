package pl.com.bottega.photostock.sales.model.client;

/**
 * Created by macie on 10.12.2016.
 */
public enum ClientStatus {
    STANDARD("Standardowy"),
    VIP("VIP"),
    GOLD("Złoty"),
    SILVER("Srebrny"),
    PLATINIUM("Platynowy");

    private String statusName;

    ClientStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
