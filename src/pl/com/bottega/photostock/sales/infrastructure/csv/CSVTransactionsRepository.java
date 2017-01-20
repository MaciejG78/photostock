package pl.com.bottega.photostock.sales.infrastructure.csv;

import com.sun.deploy.util.StringUtils;
import pl.com.bottega.photostock.sales.model.client.Transaction;
import pl.com.bottega.photostock.sales.model.client.TransactionRepository;
import pl.com.bottega.photostock.sales.model.money.Money;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by macie on 15.01.2017.
 */
public class CSVTransactionsRepository implements TransactionRepository {

    private String folderPath;
    private Transaction transaction;

    public CSVTransactionsRepository(String folderPath) {
        this.folderPath = folderPath;
    }

    public void saveTransactions(String clientNumber, Collection<Transaction> transactions) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(getRepositoryPath(clientNumber), true))) {
            for (Transaction tr : transactions) {
                String[] components = {tr.getValue().toString(), tr.getDescription(), tr.getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME)};
                printWriter.println(StringUtils.join(Arrays.asList(components), ","));
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Collection<Transaction> getTransactions(String clientNumber) {
        Collection<Transaction> transactions = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getRepositoryPath(clientNumber)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] attributes = line.trim().split(",");
                String[] costOfTransactionInString = attributes[0].split(" ");
                Money costOfTransaction = Money.valueOf(costOfTransactionInString[0]);
                String descriptionOfTransaction = attributes[1] + ", Data: " + dateAndTimeConverted(attributes[2]);
                Transaction transaction = new Transaction(costOfTransaction, descriptionOfTransaction);
                transactions.add(transaction);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("Plik nie istnieje, więc nie było żadnych transakcji");
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return transactions;
    }

    private String dateAndTimeConverted(String attribute) {
        SimpleDateFormat formatInput = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateFormatOutput = "yyyy-MM-dd";
        String hourFormatOutput = "HH:mm";
        SimpleDateFormat formatdateOutput = new SimpleDateFormat(dateFormatOutput);
        SimpleDateFormat formatHourOutput = new SimpleDateFormat(hourFormatOutput);
        String dateOutput = null;
        String hourOutput = null;
        try {
            Date dateAndTime = formatInput.parse(attribute);
            dateOutput = formatdateOutput.format(dateAndTime);
            hourOutput = formatHourOutput.format(dateAndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOutput + ", czas transakcji: " + hourOutput;
    }

    private String getRepositoryPath(String clientNumber) {
        return folderPath + File.separator + getFileName(clientNumber);
    }

    private String getFileName(String clientNumber) {
        String cs = "clients-" + clientNumber + "-transactions.csv";
        return "clients-" + clientNumber + "-transactions.csv";
    }


}

