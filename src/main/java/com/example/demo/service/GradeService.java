package com.example.demo.service;

import com.example.demo.entity.Grade;
import com.example.demo.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GradeService extends BaseService<Grade, Long, GradeRepository> {
    
    @Autowired
    public GradeService(GradeRepository repository) {
        super(repository);
        this.repository = repository;
    }
    
    public Page<Grade> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}