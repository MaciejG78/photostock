package pl.com.bottega.photostock.sales.model.client;

import java.util.Collection;

/**
 * Created by macie on 19.01.2017.
 */
public interface TransactionRepository {

    void saveTransactions(String clientNumber, Collection<Transaction> transactions);

    Collection<Transaction> getTransactions(String clientNumber);

}
