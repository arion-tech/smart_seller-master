package io.mintit.lafarge.events;

import io.mintit.lafarge.model.Reservation;

/**
 * Created by mint on 13/04/18.
 */

public class UpdateReservationEvent {
    private final Reservation reservation;

    public UpdateReservationEvent(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
