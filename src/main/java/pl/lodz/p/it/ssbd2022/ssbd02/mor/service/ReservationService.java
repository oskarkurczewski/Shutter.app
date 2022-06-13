package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReservationFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.AvailabilityFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.PhotographerFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.ReservationFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReservationService {

    @Inject
    private PhotographerFacade photographerFacade;

    @Inject
    private ReservationFacade reservationFacade;

    @Inject
    private AvailabilityFacade availabilityFacade;

    @PermitAll
    public Reservation findById(Long id) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    /**
     * Metoda służąca do tworzenia rezerwacji.
     * <p>
     * Jeżeli nie ma wolnych terminów, to nie rezerwacja nie może być utworzona.
     * Jeżeli jest więcej niż jeden termin, to nie można stworzyć rezerwacji
     *
     * @param newReservation Nowa rezerwacja
     * @throws BaseApplicationException W przypadku gdy nie można stworzyć rezerwacji
     */
    @RolesAllowed(reservePhotographer)
    public void addReservation(Reservation newReservation) throws BaseApplicationException {
        List<Availability> availability = availabilityFacade.findInPeriod(newReservation);
        // Jeżeli nie ma wolnych terminów, to nie rezerwacja nie może być utworzona.
        // Jeżeli jest więcej niż jeden termin, to nie można stworzyć rezerwacji
        if (availability.size() != 1) {
            throw ExceptionFactory.invalidReservationTimeException();
        }

        List<Reservation> reservationList = reservationFacade.findInPeriod(newReservation);
        if (reservationList.size() > 0) {
            throw ExceptionFactory.invalidReservationTimeException();
        }

        reservationFacade.persist(newReservation);
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

    /**
     * Metoda pozwalająca na uzyskanie stronicowanej listy wszystkich aktywnych w systemie fotografów
     *
     * @param page           strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @return stronicowana lista aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<PhotographerInfo> listPhotographers(int page, int recordsPerPage) throws BaseApplicationException {
        return photographerFacade.getAllPhotographersWithVisibility(true, page, recordsPerPage);
    }

    /**
     * Metoda pozwalająca na uzyskanie liczby wszystkich aktywnych fotografów o podanej widoczności
     *
     * @return liczba aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public Long countPhotographers() throws BaseApplicationException {
        return photographerFacade.countAllPhotographersWithVisibility(true);
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
