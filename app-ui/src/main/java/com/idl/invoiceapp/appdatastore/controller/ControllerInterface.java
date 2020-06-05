package com.idl.invoiceapp.appdatastore.controller;

import org.springframework.data.domain.Example;

import java.util.List;

public interface ControllerInterface<T, I> {

    List<T> findByExample(Example<T> example);

    List<T> findAll();

    T findById(I i);

    T save(T t);

    void delete(T t);
}
