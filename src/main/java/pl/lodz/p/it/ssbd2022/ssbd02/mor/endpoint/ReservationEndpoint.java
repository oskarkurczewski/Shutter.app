package pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.ReservationService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
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
