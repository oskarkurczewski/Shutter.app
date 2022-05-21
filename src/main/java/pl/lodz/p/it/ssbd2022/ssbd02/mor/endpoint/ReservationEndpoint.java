package pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.AvailabilityService;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.inject.Inject;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
public class ReservationEndpoint extends AbstractEndpoint {

    @Inject
    private ReservationService reservationService;


    @RolesAllowed(reservePhotographer)
    public void createReservation(CreateReservationDto createReservationDto) throws InvalidReservationTimeExcpetion {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(cancelReservation)
    public void cancelReservation(Long reservationId) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(discardReservation)
    public void discardReservation(Long reservationId) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(showReservations)
    public List<ReservationListEntryDto> listReservations() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(showJobs)
    public List<ReservationListEntryDto> listJobs() {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerListEntryDto> listPhotographers() {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerByAvailability(TimePeriodDto timePeriod) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerByName(String name) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerBySpeciality(String specialization) {
        throw new UnsupportedOperationException();
    }
}
