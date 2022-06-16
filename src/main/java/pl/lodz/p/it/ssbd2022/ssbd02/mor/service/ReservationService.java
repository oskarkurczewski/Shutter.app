package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.AvailabilityFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.PhotographerFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.ReservationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReservationService {

    /**
     * Metoda wyszukująca rezerwację z danym numerem ID
     *
     * @param id id rezerwacji
     * @return rezerwacja o wskazanym ID
     */
    @Inject
    private PhotographerFacade photographerFacade;

    @Inject
    private ReservationFacade reservationFacade;

    @Inject
    private EmailService emailService;

    @Inject
    private AvailabilityFacade availabilityFacade;

    @PermitAll
    public Reservation findById(Long id) throws BaseApplicationException {
        return Optional.ofNullable(reservationFacade.find(id)).orElseThrow(ExceptionFactory::noReservationFoundException);
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
            throw ExceptionFactory.invalidReservationTimeException("exception.no_availability_in_period");
        }

        List<Reservation> reservationList = reservationFacade.findInPeriod(newReservation);
        if (reservationList.size() > 0) {
            throw ExceptionFactory.invalidReservationTimeException("exception.reservation_already_exists");
        }

        reservationFacade.persist(newReservation);
    }

    /**
     * Metoda odwołująca rezerwację w imieniu wskazanego klienta
     * Klient może odwołać tylko własną rezerwację
     *
     * @param caller        login klienta odwołującego rezerwację
     * @param reservationId id rezerwacji mającej być odwołanej
     */
    @RolesAllowed(cancelReservation)
    public void cancelReservation(String caller, Long reservationId) throws BaseApplicationException {
        Reservation r = findById(reservationId);
        if (!r.getAccount().getLogin().equals(caller)) {
            throw ExceptionFactory.noReservationFoundException();
        }
        PhotographerInfo pInfo = r.getPhotographer();
        reservationFacade.remove(r);
        emailService.sendReservationCanceledEmail(pInfo.getAccount().getEmail(), r.getId(), pInfo.getAccount().getLocale());
    }

    /**
     * Metoda odrzucająca rezerwację przez fotografa
     * Fotograf może odwołać tylko własną rezerwację
     *
     * @param caller        login fotografa odwołującego rezerwację
     * @param reservationId id rezerwacji mającej być odwołanej
     */
    @RolesAllowed(discardReservation)
    public void discardReservation(String caller, Long reservationId) throws BaseApplicationException {
        Reservation r = findById(reservationId);
        if (!r.getPhotographer().getAccount().getLogin().equals(caller)) {
            throw ExceptionFactory.noReservationFoundException();
        }
        PhotographerInfo pInfo = r.getPhotographer();
        reservationFacade.remove(r);
        emailService.sendReservationDiscardedEmail(r.getAccount().getEmail(), r.getId(), pInfo.getAccount().getLocale());
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
