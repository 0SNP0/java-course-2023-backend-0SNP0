package edu.java.scrapper.repository;

import java.util.Collection;

public interface JdbcRepository<T> {
    T add(T entity);

    T remove(T entity);

    Collection<T> findAll();
}
