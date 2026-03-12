package com.mycompany.bastaewan.service;

import java.util.ArrayList;

// Abstraction: interface defines contract for all service classes
// Polymorphism: different services implement same interface
public interface Manageable<T> {
    void add(T item);
    void update(int id, T item);
    void delete(int id);
    T getById(int id);
    ArrayList<T> getAll();
    int getNextId();
}
