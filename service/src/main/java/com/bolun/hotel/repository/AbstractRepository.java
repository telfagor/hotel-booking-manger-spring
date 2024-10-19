package com.bolun.hotel.repository;

import com.bolun.hotel.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public abstract class AbstractRepository<K extends Serializable, E extends BaseEntity<K>> implements RepositoryBase<K, E> {

    protected final EntityManager entityManager;
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }

    @Override
    public Optional<E> findById(K id) {
        return ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return entityManager.createQuery(criteria)
                .getResultList();
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }
}

