package pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.*;
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
    private AuthenticationContext authCtx;

    @RolesAllowed(reservePhotographer)
    public void createReservation(CreateReservationDto createReservationDto) throws InvalidReservationTimeExcpetion {
        throw new UnsupportedOperationException();
    }

    /**
     * Metoda do anulowania rezerwacji przez klienta
     * @param reservationId id rezerwacji, która ma być anulowana
     */
    @RolesAllowed(cancelReservation)
    public void cancelReservation(Long reservationId) throws BaseApplicationException {
        String caller = authCtx.getCurrentUsersLogin();
        reservationService.cancelReservation(caller, reservationId);
    }

    @RolesAllowed(discardReservation)
    public void discardReservation(Long reservationId) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    /**
     * Metoda pozwalająca na pobieranie rezerwacji dla użytkownika (niezakończonych lub wszystkich)
     *
     * @param page              numer strony
     * @param recordsPerPage    liczba recenzji na stronę
     * @param order             kolejność sortowania względem kolumny time_from
     * @param getAll            flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException     niepowodzenie operacji
     */
    @RolesAllowed(showReservations)
    public List<ReservationListEntryDto> listReservations(int page, int recordsPerPage, String order, Boolean getAll)
            throws BaseApplicationException {
        String login = authCtx.getCurrentUsersLogin();
        Account account = accountService.findByLogin(login);

        List<Reservation> reservations = reservationService.listReservations(account, page, recordsPerPage, order, getAll);
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
     * @param page strona listy, którą należy pozyskać
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

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerByName(String name) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<PhotographerListEntryDto> findPhotographerBySpeciality(String specialization) {
        throw new UnsupportedOperationException();
    }
}
