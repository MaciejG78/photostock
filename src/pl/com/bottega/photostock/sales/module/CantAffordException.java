package pl.com.bottega.photostock.sales.module;

/**
 * Created by macie on 11.12.2016.
 */
public class CantAffordException extends RuntimeException {


    public CantAffordException(String message) {
        super(message); //wywołuje konstruktor klasy bazowej (nadrzędnej)
    }
}
