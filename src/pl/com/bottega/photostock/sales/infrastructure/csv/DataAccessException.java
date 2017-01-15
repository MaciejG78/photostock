package pl.com.bottega.photostock.sales.infrastructure.csv;

/**
 * Created by macie on 15.01.2017.
 */
public class DataAccessException extends RuntimeException {

    public DataAccessException(Exception ex){
        super(ex);
    }

}
