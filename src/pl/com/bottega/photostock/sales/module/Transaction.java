package pl.com.bottega.photostock.sales.module;

import java.time.LocalDateTime;

/**
 * Created by maciek on 11.12.2016.
 */
public class Transaction {

    private Money value;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(Money value, String description){
        this.value = value;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

}

