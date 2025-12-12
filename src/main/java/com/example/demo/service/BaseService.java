package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class BaseService<T, ID, R extends JpaRepository<T, ID>> {
    
    protected R repository;
    
    public BaseService(R repository) {
        this.repository = repository;
    }
    
    public T save(T entity) {
        return repository.save(entity);
    }
    
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }
    
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
    
    public Iterable<T> findAll() {
        return repository.findAll();
    }
}