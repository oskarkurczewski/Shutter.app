package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;
import java.time.LocalDateTime;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.reservePhotographer;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.showReservations;

@Stateless
@Interceptors({LoggingInterceptor.class, MorFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReservationFacade extends FacadeTemplate<Reservation> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public ReservationFacade() {
        super(Reservation.class);
    }

    @Override
    @RolesAllowed(reservePhotographer)
    public Reservation persist(Reservation entity) throws BaseApplicationException {
        try {
            return super.persist(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @RolesAllowed(reservePhotographer)
    public List<Reservation> findInPeriod(Reservation reservation) throws BaseApplicationException {
        TypedQuery<Reservation> query = getEm().createNamedQuery("reservation.findInPeriod", Reservation.class);
        query.setParameter("photographer", reservation.getPhotographer());
        query.setParameter("account", reservation.getAccount());
        query.setParameter("time_from", reservation.getTimeFrom());
        query.setParameter("time_to", reservation.getTimeTo());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }


    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }

    @Override
    public Reservation find(Long id) throws BaseApplicationException {
        try {
            return super.find(id);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    public void remove(Reservation entity) throws BaseApplicationException {
        try {
            super.remove(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * dodaje znak '%' na początku i na końcu struny
     *
     * @param s struna
     * @return struna wynikowa
     */
    private String addPercent(String s) {
        return "%" + s + "%";
    }


    /**
     * Metoda pozwalająca na pobieranie rezerwacji dla użytkownika (niezakończonych lub wszystkich)
     *
     * @param account        konto użytkownika, dla którego pobierane są rezerwacje
     * @param page           numer strony
     * @param recordsPerPage liczba recenzji na stronę
     * @param order          kolejność sortowania względem kolumny time_from
     * @param getAll         flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @return Reservation      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(showReservations)
    public List<Reservation> getReservationsForUser(Account account, String name, int page, int recordsPerPage, String order, Boolean getAll)
            throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Reservation> query = criteriaBuilder.createQuery(Reservation.class);
        Root<Reservation> table = query.from(Reservation.class);
        query.select(table);

        try {
            switch (order) {
                case "asc": {
                    query.orderBy(criteriaBuilder.asc(table.get("from")));
                    break;
                }
                case "desc": {
                    query.orderBy(criteriaBuilder.desc(table.get("from")));
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            throw ExceptionFactory.wrongParameterException();
        }

        if (getAll) {
            query.where(criteriaBuilder.equal(table.get("account"), account.getId()));
        } else {
            if (name != null && !name.equals("")) {
                query.where(criteriaBuilder.and(
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(table.get("account").get("name")), addPercent(name.toLowerCase())),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(table.get("account").get("surname")), addPercent(name.toLowerCase()))
                        )), (
                        (criteriaBuilder.and(
                                criteriaBuilder.equal(table.get("account"), account.getId()),
                                criteriaBuilder.greaterThan(table.get("from"), LocalDateTime.now())
                        ))

                ));
            } else {
                query.where(criteriaBuilder.and(
                        criteriaBuilder.equal(table.get("account"), account.getId()),
                        criteriaBuilder.greaterThan(table.get("from"), LocalDateTime.now())
                ));
            }

        }

        try {
            return em
                    .createQuery(query)
                    .setFirstResult(recordsPerPage * (page - 1))
                    .setMaxResults(recordsPerPage)
                    .getResultList();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }
}
