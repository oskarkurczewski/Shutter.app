package pl.lodz.p.it.ssbd2022.ssbd02.mow;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ReviewFacade extends FacadeTemplate<Review> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public ReviewFacade() {
        super(Review.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
