package edu.java.scrapper.repository;

import java.util.Collection;

public interface Repository<T> {
    T add(T entity);

    T remove(T entity);

    Collection<T> findAll();
}
