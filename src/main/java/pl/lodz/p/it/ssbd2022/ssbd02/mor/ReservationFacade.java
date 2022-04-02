package pl.lodz.p.it.ssbd2022.ssbd02.mor;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ReservationFacade extends FacadeTemplate<Reservation> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public ReservationFacade() {
        super(Reservation.class);
    }

    @Override
    public EntityManager getEm() {
        return null;
    }
}
