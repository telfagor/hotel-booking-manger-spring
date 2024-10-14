package com.bolun.hotel.repository;

import com.bolun.hotel.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface RepositoryBase<K extends Serializable, E extends BaseEntity<K>> {

    E save(E entity);

    void update(E entity);

    Optional<E> findById(K id);

    List<E> findAll();

    void delete(E id);
}
