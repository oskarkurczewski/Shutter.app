package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class AccountReportCauseFacade extends FacadeTemplate<AccountReportCause> {

    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public AccountReportCauseFacade() {
        super(AccountReportCause.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }

    @PermitAll
    public AccountReportCause getAccountReportCause(String cause) throws DataNotFoundException {
        TypedQuery<AccountReportCause> typedQuery = getEm()
                .createNamedQuery("account_report_cause.getAccountReportCause", AccountReportCause.class);
        typedQuery.setParameter("report_cause", cause);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new DataNotFoundException("exception.account_report_cause_not_found");
        }
    }
}
