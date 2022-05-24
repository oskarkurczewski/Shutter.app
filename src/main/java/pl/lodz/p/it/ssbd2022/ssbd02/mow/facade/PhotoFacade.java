package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Interceptors({LoggingInterceptor.class, FacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotoFacade extends FacadeTemplate<Photo> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public PhotoFacade() {
        super(Photo.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }
}
