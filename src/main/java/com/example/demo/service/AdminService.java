package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends BaseService<Admin, Long, AdminRepository> {
    
    @Autowired
    public AdminService(AdminRepository repository) {
        super(repository);
        this.repository = repository;
    }
    
    public Page<Admin> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}