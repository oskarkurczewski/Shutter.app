package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PhotoFacade extends FacadeTemplate<Photo> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public PhotoFacade() {
        super(Photo.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
