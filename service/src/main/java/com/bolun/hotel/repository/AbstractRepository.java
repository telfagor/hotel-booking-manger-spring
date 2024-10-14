package com.bolun.hotel.repository;

import com.bolun.hotel.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public abstract class AbstractRepository<K extends Serializable, E extends BaseEntity<K>> implements RepositoryBase<K, E> {

    private final Session session;
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        session.persist(entity);
        session.flush();
        return entity;
    }

    @Override
    public void update(E entity) {
        session.merge(entity);
        session.flush();
    }

    @Override
    public Optional<E> findById(K id) {
        return ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria)
                .getResultList();
    }

    @Override
    public void delete(E entity) {
        session.remove(entity);
        session.flush();
    }
}

