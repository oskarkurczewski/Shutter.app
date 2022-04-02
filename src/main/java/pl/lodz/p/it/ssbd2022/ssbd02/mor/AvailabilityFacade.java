package pl.lodz.p.it.ssbd2022.ssbd02.mor;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AvailabilityFacade extends FacadeTemplate<Availability> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public AvailabilityFacade() {
        super(Availability.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
