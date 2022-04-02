package pl.lodz.p.it.ssbd2022.ssbd02.mor;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photographer;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PhotographerFacade extends FacadeTemplate<Photographer> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public PhotographerFacade() {
        super(Photographer.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
