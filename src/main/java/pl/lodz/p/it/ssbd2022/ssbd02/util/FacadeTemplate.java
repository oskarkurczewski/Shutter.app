package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class FacadeTemplate<T> {

    private final Class<T> type;

    protected FacadeTemplate(Class<T> type) {
        this.type = type;
    }

    protected abstract EntityManager getEm();

    protected T persist(T entity) throws BaseApplicationException {
        getEm().persist(entity);
        getEm().flush();
        return entity;
    }

    protected void remove(T entity) throws BaseApplicationException {
        getEm().remove(entity);
        getEm().flush();
    }

    protected T update(T entity) throws BaseApplicationException {
        T updated = getEm().merge(entity);
        getEm().flush();
        return updated;
    }

    protected T find(Long id) throws BaseApplicationException {
        return getEm().find(type, id);
    }

    protected List<T> findAll() {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> vr = cq.from(type);
        cq.select(vr);
        return getEm().createQuery(cq).getResultList();
    }

}
