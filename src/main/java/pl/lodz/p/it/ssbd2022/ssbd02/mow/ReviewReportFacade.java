package pl.lodz.p.it.ssbd2022.ssbd02.mow;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ReviewReportFacade extends FacadeTemplate<ReviewReport> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public ReviewReportFacade() {
        super(ReviewReport.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }
}
