package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReservationFacade extends FacadeTemplate<Reservation> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public ReservationFacade() {
        super(Reservation.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }
}
