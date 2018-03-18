package com.akarpovich.interior.db.dao;

import java.util.List;

public interface DAO<T> {

    T findById(Integer id);

    List<T> findAll();

    boolean save(T object);

    boolean update(T object);
}
