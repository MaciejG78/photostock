package pl.com.bottega.photostock.sales.infrastructure;

import pl.com.bottega.photostock.sales.module.Reservation;
import pl.com.bottega.photostock.sales.module.ReservationRepository;
import pl.com.bottega.photostock.sales.presentation.MainScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by macie on 08.01.2017.
 */
public class InMemoryReservationRepository implements ReservationRepository{

    private static final Map<String, Reservation> REPOSITORY = new HashMap<>();

    @Override
    public void put(Reservation reservation){
        REPOSITORY.put(reservation.getNumber(), reservation);
    }

    @Override
    public Reservation get(String reservationNumber){
        return REPOSITORY.get(reservationNumber);
    }

    @Override
    public Reservation getActiveReservationForClient(String clientNumber) {
        for (Reservation reservation : REPOSITORY.values()) {
            if (reservation.isOwnedBy(clientNumber)) ;
            return reservation;
        }
        return null;
    }


}
