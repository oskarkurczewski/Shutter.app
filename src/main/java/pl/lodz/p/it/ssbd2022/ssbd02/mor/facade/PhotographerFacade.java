package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Specialization;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.WeekDay;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.reservePhotographer;

@Stateless
@Interceptors({LoggingInterceptor.class, MorFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerFacade extends FacadeTemplate<PhotographerInfo> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public PhotographerFacade() {
        super(PhotographerInfo.class);
    }

    /**
     * Metoda zwracająca encję zawierającą informacje o fotografie
     *
     * @param login login fotografa
     * @return PhotographerInfo
     * @throws BaseApplicationException
     */
    @PermitAll
    public PhotographerInfo getPhotographerByLogin(String login) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("not found");
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            System.out.println("opt lock");
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            System.out.println("persistence");
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            System.out.println("exception");
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }


    /**
     * Metoda pozwalająca na uzyskanie stronicowanej listy wszystkich fotografów o podanej widoczności
     *
     * @param visibility     widoczność fotografa, po jakiej ma być poprowadzone wyszukiwanie
     * @param page           strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @return stronicowana lista aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<PhotographerInfo> getAllPhotographersWithVisibility(
            Boolean visibility,
            int page,
            int recordsPerPage
    ) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery(
                "photographer_info.findAllWithVisibility",
                PhotographerInfo.class
        );
        query.setParameter("visibility", visibility);

        try {
            return query
                    .setFirstResult(recordsPerPage * (page - 1))
                    .setMaxResults(recordsPerPage)
                    .getResultList();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Metoda pozwalająca na uzyskanie liczby wszystkich aktywnych fotografów o podanej widoczności
     *
     * @param visibility widoczność fotografa, po jakiej ma być poprowadzone wyszukiwanie
     * @return liczba aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public Long countAllPhotographersWithVisibility(Boolean visibility)
            throws BaseApplicationException {
        TypedQuery<Long> query = getEm().createNamedQuery(
                "photographer_info.countAllWithVisibility",
                Long.class
        );
        query.setParameter("visibility", visibility);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Szuka profilu fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @see PhotographerInfo
     */
    @RolesAllowed({reservePhotographer})
    public PhotographerInfo findPhotographerByLogin(String login) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Metoda pozwalająca na uzyskanie liczby wszystkich aktywnych w systemie fotografów, których imię
     * lub nazwisko zawiera szukaną frazę
     *
     * @param name szukana fraza
     * @return liczba fotografów spełniających wymagania
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public Long countAllVisiblePhotographersByNameSurname(String name) throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<PhotographerInfo> from = criteriaQuery.from(PhotographerInfo.class);
        criteriaQuery.select(criteriaBuilder.count(from));
        this.limitToVisibleAndContainingName(criteriaQuery, criteriaBuilder, from);

        TypedQuery<Long> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setParameter("name", "%" + name.toLowerCase() + "%");
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }


    /**
     * Metoda pozwalająca na uzyskanie stronicowanej listy wszystkich aktywnych w systemie fotografów, których imię
     * lub nazwisko zawiera szukaną frazę oraz jeśli wskazano specjalizację, jest ona w liście danego fotografa
     *
     * @param name           szukana fraza
     * @param spec           specjalizacja szukanego fotografa
     * @param page           strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @param weekDay        dzień tygodnia, w którym szukani są fotografowie
     * @param fromTime       godzina, od której szukani są fotografowie
     * @param toTime         godzina, do której szukani są fotografowie
     * @return stronicowana lista aktywnych fotografów obecnych systemie, których imię lub nazwisko zawiera podaną frazę
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<PhotographerInfo> getAllVisiblePhotographersByNameSurnameSpecialization(
            String name,
            int page,
            int recordsPerPage,
            Specialization spec,
            WeekDay weekDay,
            LocalTime fromTime,
            LocalTime toTime
    ) throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PhotographerInfo> criteriaQuery = criteriaBuilder.createQuery(PhotographerInfo.class);
        Root<PhotographerInfo> from = criteriaQuery.from(PhotographerInfo.class);
        List<Predicate> predicates = new ArrayList<>();

        if (spec != null) {
            Metamodel m = em.getMetamodel();
            EntityType<PhotographerInfo> PhotographerInfo_ = m.entity(PhotographerInfo.class);
            EntityType<Specialization> Specialization_ = m.entity(Specialization.class);

            Join<PhotographerInfo, Specialization> joinSpecialization = from.join(PhotographerInfo_.getList("specializationList", Specialization.class));
            predicates.add(
                    joinSpecialization.get(Specialization_.getId(Long.class)).in(spec.getId())
            );
        }

        if (fromTime != null && toTime != null && weekDay != null) {
            Metamodel m = em.getMetamodel();
            EntityType<PhotographerInfo> PhotographerInfo_ = m.entity(PhotographerInfo.class);
            Join<PhotographerInfo, Availability> joinAvailability = from.join(PhotographerInfo_.getList("availability", Availability.class));
            predicates.add(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(joinAvailability.get("day"), weekDay),
                            criteriaBuilder.lessThanOrEqualTo(joinAvailability.get("from"), fromTime),
                            criteriaBuilder.greaterThanOrEqualTo(joinAvailability.get("to"), toTime)
                    )
            );

        }

        name = name != null ? name.toLowerCase() : "";

        predicates.add(criteriaBuilder.and(
                criteriaBuilder.equal(from.get("visible"), true),
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(from.get("account").get("name")), criteriaBuilder.parameter(String.class, "name")
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(from.get("account").get("surname")), criteriaBuilder.parameter(String.class, "name")
                        )
                )));
        
        criteriaQuery.select(from).where(predicates.toArray(new Predicate[predicates.size()]));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.quot(from.get("score"), from.get("reviewCount"))));

        TypedQuery<PhotographerInfo> typedQuery = em.createQuery(criteriaQuery);
        typedQuery.setParameter("name", "%" + name.toLowerCase() + "%");

        try {
            return typedQuery
                    .setFirstResult(recordsPerPage * (page - 1))
                    .setMaxResults(recordsPerPage)
                    .getResultList();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Funkcja pomocnicza ograniczająca wyszukanie do aktywnych fotografów, których imię lub nazwisko zawiera
     * szukaną frazę
     */
    private void limitToVisibleAndContainingName(CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder, Root<PhotographerInfo> from) {
        criteriaQuery.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(from.get("visible"), true),
                        criteriaBuilder.or(
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(from.get("account").get("name")), criteriaBuilder.parameter(String.class, "name")
                                ),
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(from.get("account").get("surname")), criteriaBuilder.parameter(String.class, "name")
                                )
                        ))

        );
    }

    @PermitAll
    public Specialization getSpecialization(String spec) throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Specialization> query = criteriaBuilder.createQuery(Specialization.class);
        Root<Specialization> root = query.from(Specialization.class);
        query.select(root).where(criteriaBuilder.equal(root.get("name"), spec));

        try {
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.specializationNotFoundException();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }
}
