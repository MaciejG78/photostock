package pl.com.bottega.photostock.sales.infrastructure.csv;

import com.sun.deploy.util.StringUtils;
import pl.com.bottega.photostock.sales.model.client.Transaction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by macie on 15.01.2017.
 */
public class CSVTransactionsRepository {

    private String folderPath;
    private Transaction transaction;

    public CSVTransactionsRepository(String folderPath) {
        this.folderPath = folderPath;
    }

    public void saveTransactions(String clientNumber, Collection<Transaction> transactions) {
        try (PrintWriter printWriter = new PrintWriter(getRepositoryPath(clientNumber))) {
            for (Transaction tr : transactions) {
                String[] components = {tr.getValue().toString(), tr.getDescription(), tr.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME)};
                printWriter.println(StringUtils.join(Arrays.asList(components), ","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO Dorobić metodę getTransactions
    public Collection<Transaction> getTransactions(String clientNumber) {
        return null;
    }

    private String getRepositoryPath(String clientNumber) {
        return folderPath + File.separator + "clients-" + clientNumber + "-transactions.csv";
    }
}
