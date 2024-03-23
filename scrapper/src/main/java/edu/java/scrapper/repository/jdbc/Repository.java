package edu.java.scrapper.repository.jdbc;

import java.util.Collection;

public interface Repository<T> {
    T add(T entity);

    T remove(T entity);

    Collection<T> findAll();
}
