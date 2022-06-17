package pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.InvalidReservationTimeException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.MorAccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReservationEndpoint extends AbstractEndpoint {

    @Inject
    private ReservationService reservationService;

    @Inject
    private MorAccountService accountService;

    @Inject
    private PhotographerService photographerService;

    @Inject
    private AuthenticationContext authenticationContext;

    /**
     * Metoda służąca do tworzenia rezerwacji.
     *
     * @param createReservationDto Dane potrzebne do utworzenia rezerwacji
     * @throws BaseApplicationException W przypadku gdy nie można stworzyć rezerwacji
     */
    @RolesAllowed(reservePhotographer)
    public void createReservation(CreateReservationDto createReservationDto) throws BaseApplicationException {
        Reservation reservation = new Reservation();
        String login = authenticationContext.getCurrentUsersLogin();
        if (login.equals(createReservationDto.getPhotographerLogin())) {
            throw ExceptionFactory.invalidReservationTimeException("exception.reservation_for_self");
        }
        reservation.setPhotographer(photographerService.getPhotographer(createReservationDto.getPhotographerLogin()));
        reservation.setAccount(accountService.findByLogin(login));
        reservation.setTimeFrom(createReservationDto.getFrom());
        reservation.setTimeTo(createReservationDto.getTo());
        reservationService.addReservation(reservation);
    }

    /**
     * Metoda do anulowania rezerwacji przez klienta
     *
     * @param reservationId id rezerwacji, która ma być anulowana
     */
    @RolesAllowed(cancelReservation)
    public void cancelReservation(Long reservationId) throws BaseApplicationException {
        String caller = authenticationContext.getCurrentUsersLogin();
        reservationService.cancelReservation(caller, reservationId);
    }

    /**
     * Metoda do anulowania rezerwacji przez fotografa
     *
     * @param reservationId id rezerwacji, która ma być anulowana
     */
    @RolesAllowed(discardReservation)
    public void discardReservation(Long reservationId) throws BaseApplicationException {
        String caller = authenticationContext.getCurrentUsersLogin();
        reservationService.discardReservation(caller, reservationId);
    }

    /**
     * Metoda pozwalająca na pobieranie rezerwacji dla użytkownika (niezakończonych lub wszystkich)
     *
     * @param name           imię lub nazwisko do wyszukania
     * @param page           numer strony
     * @param recordsPerPage liczba recenzji na stronę
     * @param order          kolejność sortowania względem kolumny time_from
     * @param getAll         flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(showReservations)
    public List<ReservationListEntryDto> listReservations(String name, int page, int recordsPerPage, String order, Boolean getAll)
            throws BaseApplicationException {
        String login = authenticationContext.getCurrentUsersLogin();
        Account account = accountService.findByLogin(login);

        List<Reservation> reservations = reservationService.listReservations(account, name, page, recordsPerPage, order, getAll);
        List<ReservationListEntryDto> reservationDtoList = new ArrayList<>();

        for (Reservation reservation : reservations) {
            reservationDtoList.add(new ReservationListEntryDto(reservation));
        }

        return reservationDtoList;
    }

    @RolesAllowed(showJobs)
    public List<ReservationListEntryDto> listJobs() {
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
    public MorListResponseDto<PhotographerListEntryDto> listPhotographers(int page, int recordsPerPage) throws BaseApplicationException {
        Long photographerCount = reservationService.countPhotographers();
        return new MorListResponseDto(
                page,
                (int) Math.ceil(photographerCount.doubleValue() / recordsPerPage),
                recordsPerPage,
                photographerCount,
                reservationService.listPhotographers(page, recordsPerPage).stream()
                        .map(PhotographerListEntryDto::new)
                        .collect(Collectors.toList())
        );
    }

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerByAvailability(TimePeriodDto timePeriod) {
        throw new UnsupportedOperationException();
    }

    /**
     * Metoda pozwalająca na uzyskanie stronicowanej listy wszystkich aktywnych w systemie fotografów, których imię
     * lub nazwisko zawiera szukaną frazę
     *
     * @param name szukana fraza
     * @param page strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @return stronicowana lista aktywnych fotografów obecnych systemie, których imię lub nazwisko zawiera podaną frazę
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public MorListResponseDto<PhotographerListEntryDto> findPhotographerByNameSurname(String name, int page, int recordsPerPage) throws BaseApplicationException {
        List<PhotographerInfo> list = reservationService.findPhotographerByNameSurname(name, page, recordsPerPage);
        Long photographerCount = (long) list.size();

        return new MorListResponseDto(
                page,
                (int) Math.ceil(photographerCount.doubleValue() / recordsPerPage),
                recordsPerPage,
                photographerCount,
                list.stream()
                        .map(PhotographerListEntryDto::new)
                        .collect(Collectors.toList())
        );
    }

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerBySpeciality(String specialization) {
        throw new UnsupportedOperationException();
    }
}
