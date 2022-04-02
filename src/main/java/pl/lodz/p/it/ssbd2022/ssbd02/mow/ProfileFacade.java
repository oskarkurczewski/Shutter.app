package pl.lodz.p.it.ssbd2022.ssbd02.mow;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Profile;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProfileFacade extends FacadeTemplate<Profile> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public ProfileFacade() {
        super(Profile.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
