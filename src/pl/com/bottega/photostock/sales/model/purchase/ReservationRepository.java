package pl.com.bottega.photostock.sales.model.purchase;

/**
 * Created by macie on 08.01.2017.
 */
public interface ReservationRepository {

    void put(Reservation reservation);

    Reservation get(String reservationNumber);

    Reservation getActiveReservationForClient(String clientNumber);

}
