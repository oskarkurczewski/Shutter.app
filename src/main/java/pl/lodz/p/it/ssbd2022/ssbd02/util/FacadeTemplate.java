package pl.lodz.p.it.ssbd2022.ssbd02.util;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class FacadeTemplate<T extends ManagedEntity> {

    private final Class<T> type;

    protected FacadeTemplate(Class<T> type) {
        this.type = type;
    }

    public abstract EntityManager getEm();

    public T persist(T entity) {
        getEm().persist(entity);
        getEm().flush();
        return entity;
    }

    public void remove(T entity) {
        getEm().remove(entity);
        getEm().flush();
    }

    public T update(T entity) {
        T updated = getEm().merge(entity);
        getEm().flush();
        return updated;
    }

    public T find(Long id) {
        return getEm().find(type, id);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = getEm().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> vr = cq.from(type);
        cq.select(vr);
        return getEm().createQuery(cq).getResultList();
    }

}
