package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.AvailabilityDto;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReservationService {

    @PermitAll
    public Reservation findById(Long id) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reservePhotographer)
    public void addReservation(Reservation newReservation) throws InvalidReservationTimeExcpetion {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(cancelReservation)
    public void cancelReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(discardReservation)
    public void discardReservation(Reservation reservation) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(showReservations)
    public List<Reservation> listReservations(Account account) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(showJobs)
    public List<Reservation> listJobs(PhotographerInfo photographer) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerInfo> listPhotographers() {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerInfo> findPhotographerByName(String name) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerInfo> findPhotographerBySpecialization(Specialization specialization) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerInfo> findPhotographerByAvailability(LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException();
    }
}
